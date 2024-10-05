module com.dinidu.lk.pmt {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;


    opens com.dinidu.lk.pmt to javafx.fxml;
    exports com.dinidu.lk.pmt;
}