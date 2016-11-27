(ns xa-07.core
  (:require [reagent.core :as r]
            [reagent-material-ui.core :refer [AppBar FlatButton RaisedButton]]
            [re-frame.core :as rf]
            [re-com.core :refer [modal-panel]]
            [pushy.core :as p]
            [bidi.bidi :as b]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [ajax.core :refer [GET POST]]
            [xa-07.ajax :refer [load-interceptors!]]
            [xa-07.handlers]
            [xa-07.main-panel :refer [login signup home-page start-sitting]]
            [xa-07.subscriptions])
  (:import goog.History))

(defn navbar []
  (r/with-let [identity (rf/subscribe [:identity])]
    [:nav.navbar.navbar-dark {:style {:backgroundColor "#453419"}}
     [:div.container-fluid]
     [:div.row {:style {:display "flex"}}
      [:div.col-xs-6
       [:a.navbar-brand {:href "/"}
        [:img.hidden-xs-down {:src "img/logo.png"}]
        [:img.hidden-sm-up {:src "img/dog.png"
                            :style {:height 70}}]]]
      [:div.col-xs-6 {:style {:display "flex"
                              :justifyContent "center"
                              :flexDirection "column"}}
       (if @identity
         [:div {:style {:display "flex"
                        :flexDirection "row"
                        :justifyContent "flex-end"}}
          [:div.hidden-sm-up
           [RaisedButton {:secondary true
                          :on-click #(js/console.log "hello")
                          :label "Log out"}]]
          [:div.hidden-xs-down
           [:a {:href "/start-sitting"}
            [RaisedButton {:secondary true
                           :label "Start sitting"}]
            [FlatButton {:style {:color :white}
                         :className "ml-1"
                         :on-click #(rf/dispatch [:logout])
                         :label "Log out"}]]]]
         [:div {:style {:display "flex"
                        :flexDirection "row"
                        :justifyContent "flex-end"}}
          [:div.hidden-sm-up
           [:a {:href "/login"}
            [RaisedButton {:secondary true
                           :label "Log in"}]]]
          [:div.hidden-xs-down
           [:a {:href "/login"}
            [RaisedButton {:secondary true
                           :label "Log in"}]]
           [:a {:href "/signup"}
            [FlatButton {:style {:color :white}
                         :className "ml-1"
                         :label "Sign up"}]]]])]]]))

(def pages
  {:home home-page
   :signup signup
   :login login
   :start-sitting start-sitting})

(def routes
  ["/" {"signup" :signup
        "login" :login
        "start-sitting" :start-sitting
        true :home}])

(defn main-panel []
   [:div
    [navbar]
    [:div.container-fluid
     [(pages @(rf/subscribe [:page]))]]])

(def history
  (p/pushy #(rf/dispatch [:set-active-page (:handler %)]) (partial b/match-route routes)))

(defn mount-components []
  (r/render [#'main-panel] (.getElementById js/document "app")))

(defn init! []
  (rf/dispatch-sync [:initialize-db])
  (rf/dispatch [:ping-auth])
  (load-interceptors!)
  (p/start! history)
  (mount-components))
