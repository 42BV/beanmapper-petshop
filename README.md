# beanmapper-petshop
Sample Spring project for the BeanMapper libraries

### API
- Get all pets (show only name and type) ->  ``` curl -X GET localhost:8080/api/pets ```
- Get pet with id 1 -> ``` curl -X GET localhost:8080/api/pets/1 ```
- Create a pet -> ``` curl -X POST localhost:8080/api/pets ```
- Update pet with id 1 -> ``` curl -X PUT localhost:8080/api/pets/1 ```
- Partial update pet with id 1 -> ``` curl -X PATCH localhost:8080/api/pets/1 ```
- Delete pet with id 1 -> ``` curl -X DELETE localhost:8080/api/pets/1 ```

Provide data for POST, PUT, PATCH request:

```
-H 'Content-Type: application/json' 
--data '{"nickname": "Graver", "birthDate": "2015-01-01", "sex": "MALE", "petTypeId": 2}'
```
