package com.example.insect.Model;

public class Insect {
    private int id;
    private String tenConTrung, dacDiem,diaBanHoatDong,cayTrongBiAnhHuong,tacHai,cachPhongNgua;

    public Insect(int id, String tenConTrung, String dacDiem, String diaBanHoatDong, String cayTrongBiAnhHuong, String tacHai, String cachPhongNgua) {
        this.id = id;
        this.tenConTrung = tenConTrung;
        this.dacDiem = dacDiem;
        this.diaBanHoatDong = diaBanHoatDong;
        this.cayTrongBiAnhHuong = cayTrongBiAnhHuong;
        this.tacHai = tacHai;
        this.cachPhongNgua = cachPhongNgua;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenConTrung() {
        return tenConTrung;
    }

    public void setTenConTrung(String tenConTrung) {
        this.tenConTrung = tenConTrung;
    }

    public String getDacDiem() {
        return dacDiem;
    }

    public void setDacDiem(String dacDiem) {
        this.dacDiem = dacDiem;
    }

    public String getDiaBanHoatDong() {
        return diaBanHoatDong;
    }

    public void setDiaBanHoatDong(String diaBanHoatDong) {
        this.diaBanHoatDong = diaBanHoatDong;
    }

    public String getCayTrongBiAnhHuong() {
        return cayTrongBiAnhHuong;
    }

    public void setCayTrongBiAnhHuong(String cayTrongBiAnhHuong) {
        this.cayTrongBiAnhHuong = cayTrongBiAnhHuong;
    }

    public String getTacHai() {
        return tacHai;
    }

    public void setTacHai(String tacHai) {
        this.tacHai = tacHai;
    }

    public String getCachPhongNgua() {
        return cachPhongNgua;
    }

    public void setCachPhongNgua(String cachPhongNgua) {
        this.cachPhongNgua = cachPhongNgua;
    }
}


