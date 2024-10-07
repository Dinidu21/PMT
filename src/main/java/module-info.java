module com.dinidu.lk.pmt {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires java.mail;
    requires java.sql;
    requires jbcrypt;

    opens com.dinidu.lk.pmt.controller to javafx.fxml;
    exports com.dinidu.lk.pmt;
}