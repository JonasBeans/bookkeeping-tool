# Book keeping tool 

## Add a user to the development database

```bash
docker exec mongodb /bin/mongosh bookkeeping --eval "db.createUser({user: 'devUser', pwd: 't', roles: [{role: 'readWrite', db: 'bookkeping'}]})"
```