8bitpeoples database
====================

### Project Structure

The two access applications are in the user-cli/ and admin-cli/ directories.
All data currently in our database can be recreated by running:

```
mysql -u pasa -p pasa < data.sql
```

from the cisc437 database server.

### Using Our Database

To run either of the admin-cli or the user-cli, cd to their directories.
Then, run 'make', and './run' to run the application. Type "help" at either
of their command lines to view usage information.

All the information contained in the old/ directory is not in current use,
and represents initial prototypes of our applications, etc.
