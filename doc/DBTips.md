#  DB Tips

## Differences Between Primary key and Unique key
- Primary key will not accept NULL values whereas Unique key can accept one NULL value.
- A table can have only primary key whereas there can be multiple unique key on a table.
- A Clustered index automatically created when a primary key is defined whereas Unique key generates the non-clustered index.

### unqiue key

1. There can be multiple unique keys defined on a table.

1. Unique Keys result in NONCLUSTERED Unique Indexes by default.

1. One or more columns make up a unique key.

1. Column may be NULL, but on one NULL per column is allowed.

1. A unique constraint can be referenced by a Foreign Key Constraint.A
unique key is also called a unique constraint. Don’t we already do that
with the primary key? Yep, we do, but a table may have several sets of
columns which you want unique. Assuming that EmployeeID is the primary
key, then we may want to place a unique constraint on GovernmentNumber
to ensure each employee has their own number.

<div><img
src="https://www.essentialsql.com/wp-content/uploads/2017/07/EmployeeTable.png"
alt="Markdown Monster icon" style="float: left; margin-right: 10px;" />
</div>


### Unique key and Primary Key Comparison

<!--<img-->
<!--src="https://www.essentialsql.com/wp-content/uploads/2017/07/PrimaryKeyVersusUniqueKey.png"-->
<!--alt="Markdown Monster icon" style="float: left; margin-right: 10px;" />-->

####  When should you use a unique key?
I would use a unique key when you have columns you know shouldn’t contain duplicates.  This becomes a great way to ensure data validation.  In our example, we used the GovernmentNumber, but many others occur.


