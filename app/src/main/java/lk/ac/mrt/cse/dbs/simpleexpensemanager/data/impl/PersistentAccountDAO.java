package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    SQLiteDatabase SQdb;
    java.io.File filename = Constants.CONTEXT.getFilesDir();
    public PersistentAccountDAO()
    {
        SQdb = SQLiteDatabase.openOrCreateDatabase(filename.getAbsolutePath() + "/sanujenp.sqlite", null);
        SQdb.execSQL("CREATE TABLE IF NOT EXISTS Account(accountNo VARCHAR(50),bankName VARCHAR(50),accountHolderName VARCHAR(50), balance NUMERIC(10,2));");
    }

    @Override
    public List<String> getAccountNumbersList() {
        Cursor resultSet = SQdb.rawQuery("Select accountNo from Account",null);
        List<String> result = new ArrayList<String>();
        resultSet.moveToFirst();
        while(!resultSet.isAfterLast())
        {
            result.add(resultSet.getString(0));
            resultSet.moveToNext();
        }
        return result;
    }

    @Override
    public List<Account> getAccountsList() {
        Cursor resultSet = SQdb.rawQuery("Select * from Account;",null);
        List<Account> result = new ArrayList<Account>();
        resultSet.moveToFirst();
        while(!resultSet.isAfterLast())
        {

            result.add( new Account(resultSet.getString(0),resultSet.getString(1),resultSet.getString(2), Double.parseDouble(resultSet.getString(3) ) ));
            resultSet.moveToNext();
        }
        return result;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor resultSet = SQdb.rawQuery("Select * from Account where accountNo='" + accountNo+"';", null);
        resultSet.moveToFirst();
        if (resultSet.isAfterLast()) {
            throw new InvalidAccountException("Account No:" + accountNo + " is not valid!");
        }
        return new Account(resultSet.getString(0), resultSet.getString(1), resultSet.getString(2), Double.parseDouble(resultSet.getString(3)));
    }

    @Override
    public void addAccount(Account account) {
        SQdb.execSQL("INSERT INTO Account VALUES('"+account.getAccountNo()+"','"+account.getBankName()+"','"+account.getAccountHolderName()+"','"+account.getBalance()+"');");
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQdb.execSQL("DELETE FROM Account WHERE accountNo='"+accountNo+"';");
    }

}
