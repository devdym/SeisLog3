package main.tension;

import java.time.LocalDate;

public class TensionTable {
    public LocalDate date_;
    public int str1;
    public int str2;
    public int str3;
    public int str4;
    public int str5;
    public int str6;

    public TensionTable(LocalDate date_, int str1, int str2, int str3, int str4, int str5, int str6) {
        this.date_ = date_;
        this.str1 = str1;
        this.str2 = str2;
        this.str3 = str3;
        this.str4 = str4;
        this.str5 = str5;
        this.str6 = str6;
    }

    public LocalDate getDate_() {
        return date_;
    }

    public void setDate_(LocalDate date_) {
        this.date_ = date_;
    }

    public int getStr1() {
        return str1;
    }

    public void setStr1(int str1) {
        this.str1 = str1;
    }

    public int getStr2() {
        return str2;
    }

    public void setStr2(int str2) {
        this.str2 = str2;
    }

    public int getStr3() {
        return str3;
    }

    public void setStr3(int str3) {
        this.str3 = str3;
    }

    public int getStr4() {
        return str4;
    }

    public void setStr4(int str4) {
        this.str4 = str4;
    }

    public int getStr5() {
        return str5;
    }

    public void setStr5(int str5) {
        this.str5 = str5;
    }

    public int getStr6() {
        return str6;
    }

    public void setStr6(int str6) {
        this.str6 = str6;
    }
}
