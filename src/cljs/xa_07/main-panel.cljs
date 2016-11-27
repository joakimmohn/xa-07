(ns xa-07.main-panel
  (:require [reagent.core :as r]
            [re-com.core :refer [slider]]
            [gmapscljs.core :refer [google-maps marker]]
            [gmapscljs.utils :refer [lat-lon] :as map.utils]
            [reagent-material-ui.core :refer [IconButton Slider Toggle Card CardHeader CardTitle CardText Paper TextField Divider RaisedButton]]
            [re-frame.core :as rf]
            [re-com.core :refer [modal-panel]]))

(defn user-card [{:keys [id full_name email rate]}]
  ^{:key id}
   [:div.col-xs-12 {:style {:padding "10"
                            :padding-right "0"}}
    [Card
      [CardHeader {:avatar "img/profile.png"
                   :title full_name}
       [CardText
        [:div
         [:p "id: " id]
         [:p "rate: " rate]
         [:p "email: " email]]
        [IconButton {:className "float-xs-right"
                     :disabled true
                     :iconClassName "fa fa-envelope-o"}]]]]])

(defn map-marker [{:keys [id xcoordinate ycoordinate]}]
  [marker ^{:key id} {:position (lat-lon xcoordinate ycoordinate)}])

(defn home-page []
  (r/with-let [users (rf/subscribe [:users])]
    (fn []
      (rf/dispatch [:get-users])
      [:div.row
         [:div.hidden-xs-down.col-sm-6 {:style {:padding-right "0px"
                                                :padding-left "5px"}}
          [google-maps {:style {:height 700}
                        :center (lat-lon 55.676098 12.568337)
                        :zoom 12}
           (map map-marker @users)]]

         [:div.col-xs-12.col-sm-6.offset-sm-6 {:style {:position "fixed"
                                                       :overflow-y "auto"
                                                       :height "100%"
                                                       :padding-left "0"}}
           (map user-card @users)]])))



(defn login []
 (r/with-let [form (r/atom {:email ""
                            :password ""})]
  [:div.container-fluid {:style {"display" "flex"
                                 "justifyContent" "center"}}
      [:div.col-xs-12 {:style {"maxWidth" "25em"}}
       [Paper {:zdepth 2}
        [:div.container.text-xs-center
         [:div.col-xs-8.offset-xs-2.mt-2
          [:h2 "Log in"]]
         [TextField {:floatingLabelText "Email"
                     :value (:email @form)
                     :onChange #(swap! form assoc :email (-> % .-target .-value))}]
         [TextField {:floatingLabelText "Password"
                     :type :password
                     :value (:password @form)
                     :onChange #(swap! form assoc :password (-> % .-target .-value))}]
         [:div.text-xs-center.my-1
          [RaisedButton {:primary true
                         :label "submit"
                         :on-click #(do
                                     (rf/dispatch [:login @form])
                                     (reset! form {:email "" :password ""}))}]]]]]]))

(defn signup []
  (r/with-let [form (r/atom {:email ""
                             :full-name ""
                             :password ""
                             :cpassword ""})]
   [:div.container-fluid {:style {"display" "flex"
                                  "justifyContent" "center"}}
     [:div.col-xs-12 {:style {"maxWidth" "25em"}}
      [Paper {:zdepth 2}
       [:div.container.text-xs-center
        [:div.col-xs-8.offset-xs-2.mt-2
         [:h2 "Sign up"]]
        [TextField {:floatingLabelText "Email"
                    :value (:email @form)
                    :onChange #(swap! form assoc :email (-> % .-target .-value))}]
        [TextField {:floatingLabelText "Full name"
                    :value (:full-name @form)
                    :onChange #(swap! form assoc :full-name (-> % .-target .-value))}]
        [TextField {:floatingLabelText "Password"
                    :type :password
                    :value (:password @form)
                    :onChange #(swap! form assoc :password (-> % .-target .-value))}]
        [TextField {:floatingLabelText "Confirm password"
                    :type :password
                    :value (:cpassword @form)
                    :onChange #(swap! form assoc :cpassword (-> % .-target .-value))}]
        [:div.text-xs-center.my-1
         [RaisedButton {:primary true
                        :label "submit"
                        :on-click #(do
                                     (rf/dispatch [:create-user @form])
                                     (reset! form {:email ""
                                                   :full-name ""
                                                   :password ""
                                                   :cpassword ""}))}]]]]]]))

(defn start-sitting []
  (r/with-let [identity (rf/subscribe [:identity])
               form (r/atom {:id (get-in @identity [:user :id])
                             :xcoordinate 0
                             :ycoordinate 0
                             :description ""
                             :rate 150
                             :active false})]
   [:div.container-fluid {:style {"display" "flex"
                                  "justifyContent" "center"}}
     [:div.col-xs-12 {:style {"maxWidth" "25em"}}
      [Paper {:zdepth 2}
       [:div.container
        [:div.col-xs-10.offset-xs-1.mt-2
         [:h2 "Start dog sitting"]]
        [:div.col-xs-10.offset-xs-1
         [TextField {:floatingLabelText "xcoordinate"
                     :value (:xcoordinate @form)
                     :onChange #(swap! form assoc :xcoordinate (js/parseInt (-> % .-target .-value)))}]
         [TextField {:floatingLabelText "ycoordinate"
                     :value (:ycoordinate @form)
                     :onChange #(swap! form assoc :ycoordinate (js/parseInt (-> % .-target .-value)))}]

         [TextField {:floatingLabelText "Tell us about yourself"
                     :value (:description @form)
                     :multiLine true
                     :rows 3
                     :onChange #(swap! form assoc :description (-> % .-target .-value))}]
         [:div.my-1
          [:p {:style {:margin-bottom 2}} "Daily rate: " [:strong (:rate @form) "kr"]]
          [slider
           :width "100%"
           :min 50
           :max 500
           :step 5
           :model (:rate @form)
           :on-change #(swap! form assoc :rate %)]]

         [Toggle {:label "Appear as active sitter:"
                   :onToggle (fn [e v]
                              (swap! form assoc :active v))}]
         [:div.text-xs-center.py-2
          [RaisedButton {:primary true
                         :label "update"
                         :on-click #(rf/dispatch [:update-user @form])}]]]]]]]))
