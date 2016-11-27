-- :name create-user! :! :n
-- :doc creates a new user record
INSERT INTO users
(id, full_name, email, pass)
VALUES (:id, :full-name, :email, :pass)

-- :name update-user! :! :n
-- :doc update an existing user record
UPDATE users
SET xcoordinate = :xcoordinate,
    ycoordinate = :ycoordinate,
    description = :description,
    rate = :rate,
    active = :active
WHERE id = :id

-- :name get-users :? :*
-- :doc retrieve all active users
SELECT id, xcoordinate, ycoordinate, full_name, email, phonenumber, description, rate, active FROM users
WHERE active = true

-- :name get-user :? :1
-- :doc retrieve all active users
SELECT id, full_name, pass, email, phonenumber, description, rate, active FROM users
WHERE email = :email

-- :name user-by-id :? :1
-- :doc retrieve all active users
SELECT id, full_name, email, phonenumber, description, rate, active FROM users
WHERE id = :id

-- :name delete-user! :! :n
-- :doc delete a user given the id
DELETE FROM users
WHERE id = :id
