/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.dao;

import com.app.beans.BankAccount;
import com.app.beans.BankAccountTransaction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author Ketkee Aryamane
 */
public class BankAccountDao {

    private DataSource dataSource;

    public BankAccountDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<BankAccount> getAllBankAccounts() {

        List<BankAccount> accounts = new ArrayList<>();
        BankAccount account = null;
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("select * from bank_account");
            while (set.next()) {
                account = new BankAccount();
                account.setAccNumber(set.getInt("acc_number"));
                account.setName(set.getString("acc_holder_name"));
                account.setEmail(set.getString("acc_email"));
                account.setAccType(set.getString("acc_type"));
                accounts.add(account);
            }

            return accounts;
        } catch (SQLException ex) {
            Logger.getLogger(BankAccountDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return accounts;
    }

    public List<BankAccountTransaction> getTransactionsForAccount(BankAccount account) {
        BankAccountTransaction transaction = null;
        List<BankAccountTransaction> transactions = new ArrayList<>();
        try {
            Connection connection = dataSource.getConnection();

            PreparedStatement statement = connection.prepareStatement("select * from bank_account_transaction where acc_number=?");
            statement.setInt(1, account.getAccNumber());
            ResultSet set = statement.executeQuery();

            while (set.next()) {
                transaction = new BankAccountTransaction();
                transaction.setAccNumber(set.getInt("acc_number"));
                transaction.setAmount(set.getDouble("amount"));
                transaction.setTxDate(new Date(set.getDate("transaction_date").getTime()));
                transaction.setTxId(set.getInt("tx_id"));
                transaction.setTxType(set.getString("transaction_type"));
                transactions.add(transaction);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BankAccountDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return transactions;
    }
}
