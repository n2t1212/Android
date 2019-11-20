package com.mimi.mimigroup.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

import com.mimi.mimigroup.model.DM_Customer;
import com.mimi.mimigroup.model.DM_Customer_Distance;
import com.mimi.mimigroup.model.DM_Customer_Search;
import com.mimi.mimigroup.model.DM_Employee;
import com.mimi.mimigroup.model.DM_Product;
import com.mimi.mimigroup.model.DM_Season;
import com.mimi.mimigroup.model.DM_Tree;
import com.mimi.mimigroup.model.DM_Tree_Disease;
import com.mimi.mimigroup.model.HT_PARA;
import com.mimi.mimigroup.model.DM_Province;
import com.mimi.mimigroup.model.DM_District;
import com.mimi.mimigroup.model.DM_Ward;
import com.mimi.mimigroup.model.QR_QRSCAN;
import com.mimi.mimigroup.model.SM_CustomerPay;
import com.mimi.mimigroup.model.SM_Order;
import com.mimi.mimigroup.model.SM_OrderDelivery;
import com.mimi.mimigroup.model.SM_OrderDeliveryDetail;
import com.mimi.mimigroup.model.SM_OrderDetail;
import com.mimi.mimigroup.model.SM_OrderStatus;
import com.mimi.mimigroup.model.SM_ReportSaleRep;
import com.mimi.mimigroup.model.SM_ReportSaleRepActivitie;
import com.mimi.mimigroup.model.SM_ReportSaleRepDisease;
import com.mimi.mimigroup.model.SM_ReportSaleRepMarket;
import com.mimi.mimigroup.model.SM_ReportSaleRepSeason;
import com.mimi.mimigroup.model.SM_ReportTech;
import com.mimi.mimigroup.model.SM_ReportTechActivity;
import com.mimi.mimigroup.model.SM_ReportTechCompetitor;
import com.mimi.mimigroup.model.SM_ReportTechDisease;
import com.mimi.mimigroup.model.SM_ReportTechMarket;
import com.mimi.mimigroup.model.SM_VisitCard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBGimsHelper extends SQLiteOpenHelper{
    private Context mCxt;
    private static DBGimsHelper mInstance = null;

    private static final int DATABASE_VERSION=12;
    private static final String DATABASE_NAME="mimigroup_sm";

    private DBGimsHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DBGimsHelper getInstance(Context ctx) {
        /**
         * use the application context as suggested by CommonsWare.
         * this will ensure that you dont accidentally leak an Activitys
         * context (see this article for more information:
         * http://android-developers.blogspot.nl/2009/01/avoiding-memory-leaks.html)
         */
        if (mInstance == null) {
            mInstance = new DBGimsHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    //UPDATE DB INFOR


    @Override
    public void onCreate(SQLiteDatabase db) {
        /*<<THONG TIN TABLE>>
         * 1-tblTHIETBI,2-tblDMTINH,3-tblDMHUYEN,4-tblDMXA,
         5-tblDMCAYTRONG,6-tblDMGIAIDOAN,7-tblDMDICHHAI,8-tblDMNHOMSP,9-tblDMSANPHAM,
         10-tblDMTANSUAT,11-tblDMMUCDO,12-tblDMDPDOITHU,13-tblDMNVKH
         14-tblKHBH,15-tblCTKHBH,16-tblKHACHHANG,17-tblNHACC,18-tblLICHTHAM,
         19-tblTHETHAM,20-tblTHITRUONG,21- tblDOITHU,22-tblTTSANPHAM,23-tblDUBAO
         <<END>>
         */

        //<<THAM SỐ HỆ THỐNG>>// INTEGER PRIMARY KEY
        String sqlCMM = "CREATE TABLE HT_PARA(" +
                "PARA_NAME VARCHAR(50)," +
                "PARA_VAL VARCHAR(50))";
        db.execSQL(sqlCMM);

        sqlCMM = "CREATE TABLE DM_PROVINCE(Provinceid INTEGER PRIMARY KEY," +
                "ProvinceCode VARCHAR(10)," +
                "Province VARCHAR(50)," +
                "ZoneCode VARCHAR(10))";
        db.execSQL(sqlCMM);

        sqlCMM = "CREATE TABLE DM_DISTRICT(Districtid INTEGER PRIMARY KEY," +
                "Provinceid INTEGER," +
                "District VARCHAR(50)," +
                "DistrictCode VARCHAR(10))";
        db.execSQL(sqlCMM);

        sqlCMM = "CREATE TABLE DM_WARD(Wardid INTEGER PRIMARY KEY," +
                "Districtid INTEGER," +
                "Ward VARCHAR(50)," +
                "WardCode VARCHAR(10))";
        db.execSQL(sqlCMM);

        sqlCMM = "CREATE TABLE DM_PRODUCT_GROUP(ProductGroupid VARCHAR(15) PRIMARY KEY," +
                "ProductGroup VARCHAR(50)," +
                "ProductGroupCode VARCHAR(10))";
        db.execSQL(sqlCMM);

        sqlCMM = "CREATE TABLE DM_PRODUCT(ProductCode VARCHAR(15) PRIMARY KEY," +
                "ProductGroupID VARCHAR(15)," +
                "ProductName VARCHAR(50)," +
                "Unit VARCHAR(30)," +
                "Spec VARCHAR(30)," +
                "ConvertKgl FLOAT," +
                "ConvertBox FLOAT," +
                "isMain BIT)";
        db.execSQL(sqlCMM);

        sqlCMM = "CREATE TABLE DM_CUSTOMER(Customerid VARCHAR(50)PRIMARY KEY," +
                "Employeeid VARCHAR(50)," +
                "CustomerCode VARCHAR(15)," +
                "CustomerName VARCHAR(150)," +
                "ShortName VARCHAR(50)," +
                "Represent VARCHAR(70)," +
                "Provinceid INTEGER," +
                "Districtid INTEGER," +
                "Wardid INTEGER," +
                "Street VARCHAR(250)," +
                "Address VARCHAR(250)," +
                "Tax VARCHAR(15)," +
                "Tel VARCHAR(15)," +
                "Fax VARCHAR(15)," +
                "Email VARCHAR(50)," +
                "IsLevel INTEGER," +
                "VisitSchedule VARCHAR(10)," +
                "Ranked VARCHAR(10)," +
                "ExtCustomer BIT," +
                "Longitude FLOAT," +
                "Latitude FLOAT," +
                "LongitudeTemp FLOAT," +
                "LatitudeTemp FLOAT," +
                "Scope FLOAT," +
                "LocationAddress VARCHAR(255)," +
                "LocationAddressTemp VARCHAR(255)," +
                "Notes VARCHAR(200)," +
                "IsSupport BIT," +
                "EditStatus INTEGER)";
        db.execSQL(sqlCMM);

        sqlCMM = "CREATE TABLE DM_CUSTOMER_SUPPLIER(Supplierid VARCHAR(50)," +
                "Customerid VARCHAR(50)," +
                "Notes VARCHAR(50))";
        db.execSQL(sqlCMM);


        sqlCMM = "CREATE TABLE QR_QRSCAN(Qrscanid VARCHAR(50)," +
                "Customerid VARCHAR(50)," +
                "CommandNo VARCHAR(15)," +
                "Qrid VARCHAR(50)," +
                "ProductCode VARCHAR(15)," +
                "ProductName VARCHAR(70)," +
                "Unit VARCHAR(30)," +
                "Specification VARCHAR(50)," +
                "ScanNo INTEGER," +
                "ScanDay DATETIME," +
                "Longitude FLOAT," +
                "Latitude FLOAT," +
                "LocationAddress VARCHAR(255)," +
                "ScanSupportID VARCHAR(50)," +
                "Imei VARCHAR(50)," +
                "ImeiSim VARCHAR(50)," +
                "ScanType VARCHAR(5)," +
                "SyncDay DATETIME," +
                "IsSync BIT)";
        db.execSQL(sqlCMM);


        sqlCMM = "CREATE TABLE QR_QRSCAN_HIS(Scanhisid VARCHAR(50)," +
                "Customerid VARCHAR(50)," +
                "CommandNo VARCHAR(15)," +
                "Qrid VARCHAR(50)," +
                "ProductCode VARCHAR(15)," +
                "ProductName VARCHAR(70)," +
                "Unit VARCHAR(30)," +
                "Specification VARCHAR(50)," +
                "ScanNo INTEGER," +
                "ScanDay DATETIME," +
                "Longitude FLOAT," +
                "Latitude FLOAT," +
                "LocationAddress VARCHAR(255)," +
                "Employee VARCHAR(100))";

        db.execSQL(sqlCMM);

        sqlCMM = "CREATE TABLE DM_CUSTOMER_LOCATION(" +
                "Customerid VARCHAR(50)," +
                "CustomerCode VARCHAR(15)," +
                "CustomerName VARCHAR(150)," +
                "LongitudeScan FLOAT," +
                "LatitudeScan FLOAT," +
                "LocationAddressScan VARCHAR(255)," +
                "Distince FLOAT)";
        db.execSQL(sqlCMM);


        sqlCMM = "CREATE TABLE SM_VISITCARD(" +
                "VisitCardID VARCHAR(50)," +
                "VisitDay DATETIME," +
                "CustomerID VARCHAR(50)," +
                "VisitType VARCHAR(5)," +
                "VisitTime DATETIME," +
                "Longitude FLOAT," +
                "Latitude FLOAT," +
                "LocationAddress VARCHAR(255)," +
                "VisitNotes VARCHAR(255)," +
                "IsSync BIT," +
                "SyncDay DATETIME)";

        db.execSQL(sqlCMM);
        // addTrace(db);

        sqlCMM = "CREATE TABLE DM_EMPLOYEE(" +
                "Employeeid VARCHAR(50)," +
                "EmployeeCode VARCHAR(15)," +
                "EmployeeName VARCHAR(120)," +
                "Notes VARCHAR(255))";
        db.execSQL(sqlCMM);


        /////////////////////////////////////// TIVA ///////////////////////////////////
        // DM_ProductPrice
        sqlCMM = "CREATE TABLE DM_PRODUCT_PRICE(" +
                "PriceID nvarchar(50)," +
                "CustomerID nvarchar(50)," +
                "ProvinceCode nvarchar(50)," +
                "ProductID nvarchar(50)," +
                "OriginPrice numeric(18, 3)," +
                "SalePrices numeric(18, 3)," +
                "SalePrice numeric(18, 3)," +
                "IsActive int ," +
                "IsActiveDate datetime," +
                "Sync varchar(500))";
        db.execSQL(sqlCMM);

        //DM_SaleOrder
        sqlCMM = "CREATE TABLE SM_ORDER(" +
                "OrderID VARCHAR(50) PRIMARY KEY," +
                "OrderCode VARCHAR(50)," +
                "CustomerID VARCHAR(50), " +
                "OrderDate DATETIME," +
                "RequestDate DATETIME," +
                "MaxDebt INTEGER," +
                "OriginMoney FLOAT," +
                "VAT FLOAT," +
                "VATMoney FLOAT," +
                "TotalMoney FLOAT," +
                "OrderStatus INTEGER," +
                "OrderNotes VARCHAR(255), " +
                "ApproveDate DATETIME," +
                "HandleStaff VARCHAR(100)," +
                "DeliveryDesc VARCHAR(500)," +
                "Longitude FLOAT," +
                "Latitude FLOAT," +
                "LocationAddress VARCHAR(255)," +
                "SeqnoCode INTEGER," +
                "IsSample BIT," +
                "IsPost BIT," +
                "PostDay DATETIME)";
        db.execSQL(sqlCMM);

        // SALE ORDER DETAIL,Notes2: TreeList
        sqlCMM = "CREATE TABLE SM_ORDER_DETAIL(" +
                "OrderDetailID VARCHAR(50) PRIMARY KEY," +
                "OrderID VARCHAR(50)," +
                "ProductID VARCHAR(50)," +
                "ProductCode VARCHAR(50)," +
                "ProductName VARCHAR(100)," +
                "Unit VARCHAR(30)," +
                "Spec VARCHAR(50)," +
                "ConvertBox FLOAT," +
                "Amount INTEGER," +
                "AmountBox FLOAT," +
                "Price FLOAT," +
                "OriginMoney FLOAT," +
                "Notes VARCHAR(255)," +
                "Notes2 VARCHAR(255))";
        db.execSQL(sqlCMM);

        // DELIVERY ORDER
        sqlCMM = "CREATE TABLE SM_DELIVERY_ORDER(" +
                "DeliveryOrderID VARCHAR(50) PRIMARY KEY," +
                "OrderID VARCHAR(50)," +
                "TransportCode VARCHAR(50)," +
                "NumberPlate VARCHAR(50)," +
                "CarType VARCHAR(100)," +
                "DeliveryStaff VARCHAR(100)," +
                "DeliveryNum INTEGER," +
                "DeliveryDate DATETIME," +
                "HandlingStaff VARCHAR(100)," +
                "HandlingDate DATETIME," +
                "TotalMoney FLOAT," +
                "DeliveryDesc VARCHAR(1500))";
        db.execSQL(sqlCMM);
        //DELIVERYDETAIL
        sqlCMM = "CREATE TABLE SM_DELIVERY_ORDER_DETAIL(" +
                "DeliveryOrderDetailID VARCHAR(50) PRIMARY KEY," +
                "DeliveryOrderID VARCHAR(50)," +
                "ProductCode VARCHAR(50)," +
                "ProductName VARCHAR(100)," +
                "Unit VARCHAR(30)," +
                "Spec VARCHAR(50)," +
                "Amount INTEGER," +
                "AmountBox FLOAT," +
                "Price FLOAT," +
                "OriginMoney FLOAT," +
                "Notes VARCHAR(255))";
        db.execSQL(sqlCMM);
        /////////////////////////////////////////END TIVA ///////////////////////////////////
        ////////////GIAI DOAN 2//////////
        sqlCMM = "CREATE TABLE SM_CUSTOMER_PAY(PayID VARCHAR(50) PRIMARY KEY," +
                "PayCode VARCHAR(20)," +
                "PayDate DATETIME," +
                "PayName VARCHAR(100)," +
                "CustomerID VARCHAR(50)," +
                "PayMoney FLOAT," +
                "PayPic VARCHAR(255)," +
                "Longitude FLOAT," +
                "Latitude FLOAT," +
                "LocationAddress VARCHAR(255)," +
                "PayStatus INTEGER," +
                "PayNotes VARCHAR(255)," +
                "IsPost BIT," +
                "PostDay DATETIME)";
        db.execSQL(sqlCMM);

        sqlCMM = "CREATE TABLE DM_TREE(TreeID INTEGER PRIMARY KEY," +
                "TreeCode VARCHAR(15)," +
                "TreeGroupCode VARCHAR(15)," +
                "TreeName VARCHAR(50))";
        db.execSQL(sqlCMM);
        sqlCMM = "CREATE TABLE DM_TREE_DISEASE(DiseaseID INTEGER PRIMARY KEY," +
                "TreeID INTEGER," +
                "DiseaseCode VARCHAR(15)," +
                "DiseaseName VARCHAR(50))";
        db.execSQL(sqlCMM);

        sqlCMM = "CREATE TABLE DM_SEASON(SeasonID INTEGER PRIMARY KEY," +
                "SeasonCode VARCHAR(15)," +
                "SeasonName VARCHAR(50))";
        db.execSQL(sqlCMM);

        //[[[[REPORT-TECH:BÁO CÁO KỸ THUẬT]]]]
        sqlCMM = "CREATE TABLE SM_REPORT_TECH(ReportTechID VARCHAR(50) PRIMARY KEY," +
                "ReportCode VARCHAR(20)," +
                "ReportName VARCHAR(50)," +
                "ReportDay DATETIME," +
                "Longitude FLOAT," +
                "Latitude FLOAT," +
                "LocationAddress VARCHAR(255)," +
                "ReceiverList VARCHAR(255)," +
                "Notes VARCHAR(50)," +
                "IsStatus INTEGER," +
                "IsPost BIT," +
                "PostDay DATETIME)";
        db.execSQL(sqlCMM);
        //THỊ TRƯỜNG
        sqlCMM = "CREATE TABLE SM_REPORT_TECH_MARKET(MarketID VARCHAR(50) PRIMARY KEY," +
                "ReportTechID VARCHAR(50)," +
                "Title VARCHAR(50)," +
                "Notes VARCHAR(200)," +
                "Useful VARCHAR(200)," +
                "Harmful VARCHAR(200))";
        db.execSQL(sqlCMM);
        //DỊCH BỆNH
        sqlCMM = "CREATE TABLE SM_REPORT_TECH_DISEASE(DiseaseID VARCHAR(50) PRIMARY KEY," +
                "ReportTechID VARCHAR(50)," +
                "TreeCode VARCHAR(15)," +
                "Title VARCHAR(50)," +
                "Acreage FLOAT," +
                "Disease VARCHAR(200)," +
                "Price FLOAT," +
                "Notes VARCHAR(200))";
        db.execSQL(sqlCMM);
        //ĐỐI THỦ
        sqlCMM = "CREATE TABLE SM_REPORT_TECH_COMPETITOR(CompetitorID VARCHAR(50) PRIMARY KEY," +
                "ReportTechID VARCHAR(50)," +
                "Title VARCHAR(50)," +
                "Notes VARCHAR(200)," +
                "Useful VARCHAR(200)," +
                "Harmful VARCHAR(200))";
        db.execSQL(sqlCMM);
        //HOẠT ĐỘNG [ISTYPE=0:TRONG TUAN, 1: KẾ HOẠCH TUẦN TỚI]
        sqlCMM = "CREATE TABLE SM_REPORT_TECH_ACTIVITIE(ActivitieID VARCHAR(50) PRIMARY KEY," +
                "ReportTechID VARCHAR(50)," +
                "IsType INTEGER," +
                "Title VARCHAR(50)," +
                "Notes VARCHAR(200)," +
                "Achievement VARCHAR(200))";
        db.execSQL(sqlCMM);


        //[[[[REPORT-SALEREP:BÁO CÁO SALES-REP]]]]
        sqlCMM = "CREATE TABLE SM_REPORT_SALEREP(ReportSaleID VARCHAR(50) PRIMARY KEY," +
                "ReportCode VARCHAR(20)," +
                "ReportName VARCHAR(50)," +
                "ReportDay DATETIME," +
                "Longitude FLOAT," +
                "Latitude FLOAT," +
                "LocationAddress VARCHAR(255)," +
                "ReceiverList VARCHAR(255)," +
                "Notes VARCHAR(50)," +
                "IsStatus INTEGER," +
                "IsPost BIT," +
                "PostDay DATETIME)";
        db.execSQL(sqlCMM);
        //THỊ TRƯỜNG
        sqlCMM = "CREATE TABLE SM_REPORT_SALEREP_MARKET(MarketID VARCHAR(50) PRIMARY KEY," +
                "ReportSaleID VARCHAR(50)," +
                "CustomerID VARCHAR(50)," +
                "CompanyName VARCHAR(100)," +
                "ProductCode VARCHAR(20)," +
                "Notes VARCHAR(200)," +
                "Price FLOAT)";
        db.execSQL(sqlCMM);
        //DỊCH BỆNH
        sqlCMM = "CREATE TABLE SM_REPORT_SALEREP_DISEASE(DiseaseID VARCHAR(50) PRIMARY KEY," +
                "ReportSaleID VARCHAR(50)," +
                "TreeCode VARCHAR(15)," +
                "Title VARCHAR(50)," +
                "Acreage FLOAT," +
                "Notes VARCHAR(200))";
        db.execSQL(sqlCMM);
        //MÙA VỤ
        sqlCMM = "CREATE TABLE SM_REPORT_SALEREP_SEASON(SeasonID VARCHAR(50) PRIMARY KEY," +
                "ReportSaleID VARCHAR(50)," +
                "TreeCode VARCHAR(15)," +
                "SeasonCode VARCHAR(15)," +
                "Title VARCHAR(50)," +
                "Acreage FLOAT," +
                "Notes VARCHAR(200))";
        db.execSQL(sqlCMM);
        //HOAT ĐỘNG - CÔNG VIỆC - LỊCH CÔNG TÁC: iStYPE: 0 : Công việc, 1: Lịch công tác
        sqlCMM = "CREATE TABLE SM_REPORT_SALEREP_ACTIVITIE(ActivitieID VARCHAR(50) PRIMARY KEY," +
                "ReportSaleID VARCHAR(50)," +
                "IsType VARCHAR(15)," +
                "Workday VARCHAR(15)," +
                "Title VARCHAR(50)," +
                "Place VARCHAR(100)," +
                "Notes VARCHAR(200))";
        db.execSQL(sqlCMM);

        // KE HOACH BAN HANG
        sqlCMM = "CREATE TABLE SM_PLAN_SALE(PlanID VARCHAR(50) PRIMARY KEY," +
                "PlanCode NVARCHAR(20)," +
                "PlanDay DATETIME," +
                "StartDay DATETIME," +
                "EndDay DATETIME," +
                "PlanName NVARCHAR(50)," +
                "PostDay DATETIME," +
                "IsPost BIT," +
                "IsStatus INTEGER," +
                "Notes NVARCHAR(50))";
        db.execSQL(sqlCMM);

        sqlCMM = "CREATE TABLE SM_PLAN_SALE_DETAIL(PlanDetailID VARCHAR(50) PRIMARY KEY," +
                "PlanID NVARCHAR(50)," +
                "CustomerID NVARCHAR(50)," +
                "ProductCode NVARCHAR(20)," +
                "AmountBox FLOAT," +
                "Amount FLOAT," +
                "Notes NVARCHAR(255)," +
                "Notes2 NVARCHAR(255))";
        db.execSQL(sqlCMM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            String sqlCMM="";
            if (oldVersion <12) {
                sqlCMM="CREATE TABLE SM_CUSTOMER_PAY(PayID VARCHAR(50) PRIMARY KEY,"+
                        "PayCode VARCHAR(20),"+
                        "PayDate DATETIME,"+
                        "PayName VARCHAR(100),"+
                        "CustomerID VARCHAR(50),"+
                        "PayMoney FLOAT,"+
                        "PayPic VARCHAR(255),"+
                        "Longitude FLOAT," +
                        "Latitude FLOAT," +
                        "LocationAddress VARCHAR(255)," +
                        "PayStatus INTEGER," +
                        "PayNotes VARCHAR(255)," +
                        "IsPost BIT,"+
                        "PostDay DATETIME)";
                db.execSQL(sqlCMM);

                db.execSQL("DROP TABLE IF EXISTS DM_TREE");
                sqlCMM="CREATE TABLE DM_TREE(TreeID INTEGER PRIMARY KEY,"+
                        "TreeCode VARCHAR(15),"+
                        "TreeGroupCode VARCHAR(15),"+
                        "TreeName VARCHAR(50))";
                db.execSQL(sqlCMM);

                sqlCMM="CREATE TABLE DM_TREE_DISEASE(DiseaseID INTEGER PRIMARY KEY,"+
                        "TreeID INTEGER,"+
                        "DiseaseCode VARCHAR(15),"+
                        "DiseaseName VARCHAR(50))";
                db.execSQL(sqlCMM);


                sqlCMM="CREATE TABLE DM_SEASON(SeasonID INTEGER PRIMARY KEY,"+
                        "SeasonCode VARCHAR(15),"+
                        "SeasonName VARCHAR(50))";
                db.execSQL(sqlCMM);

                //[[[[REPORT-TECH:BÁO CÁO KỸ THUẬT]]]]
                sqlCMM="CREATE TABLE SM_REPORT_TECH(ReportTechID VARCHAR(50) PRIMARY KEY,"+
                        "ReportCode VARCHAR(20),"+
                        "ReportName VARCHAR(50),"+
                        "ReportDay DATETIME,"+
                        "Longitude FLOAT," +
                        "Latitude FLOAT," +
                        "LocationAddress VARCHAR(255)," +
                        "ReceiverList VARCHAR(255),"+
                        "Notes VARCHAR(50),"+
                        "IsStatus INTEGER,"+
                        "IsPost BIT,"+
                        "PostDay DATETIME)";
                db.execSQL(sqlCMM);
                //THỊ TRƯỜNG
                sqlCMM="CREATE TABLE SM_REPORT_TECH_MARKET(MarketID VARCHAR(50) PRIMARY KEY,"+
                        "ReportTechID VARCHAR(50),"+
                        "Title VARCHAR(50),"+
                        "Notes VARCHAR(200)," +
                        "Useful VARCHAR(200),"+
                        "Harmful VARCHAR(200))";
                db.execSQL(sqlCMM);
                //DỊCH BỆNH
                sqlCMM="CREATE TABLE SM_REPORT_TECH_DISEASE(DiseaseID VARCHAR(50) PRIMARY KEY,"+
                        "ReportTechID VARCHAR(50),"+
                        "TreeCode VARCHAR(15),"+
                        "Title VARCHAR(50),"+
                        "Acreage FLOAT," +
                        "Disease VARCHAR(200),"+
                        "Price FLOAT,"+
                        "Notes VARCHAR(200))";
                db.execSQL(sqlCMM);
                //ĐỐI THỦ
                sqlCMM="CREATE TABLE SM_REPORT_TECH_COMPETITOR(CompetitorID VARCHAR(50) PRIMARY KEY,"+
                        "ReportTechID VARCHAR(50),"+
                        "Title VARCHAR(50),"+
                        "Notes VARCHAR(200),"+
                        "Useful VARCHAR(200),"+
                        "Harmful VARCHAR(200))";
                db.execSQL(sqlCMM);
                //HOẠT ĐỘNG [ISTYPE=0:TRONG TUAN, 1: KẾ HOẠCH TUẦN TỚI]
                sqlCMM="CREATE TABLE SM_REPORT_TECH_ACTIVITIE(ActivitieID VARCHAR(50) PRIMARY KEY,"+
                        "ReportTechID VARCHAR(50),"+
                        "IsType INTEGER,"+
                        "Title VARCHAR(50),"+
                        "Notes VARCHAR(200),"+
                        "Achievement VARCHAR(200))";
                db.execSQL(sqlCMM);


                //[[[[REPORT-SALEREP:BÁO CÁO SALES-REP]]]]
                sqlCMM="CREATE TABLE SM_REPORT_SALEREP(ReportSaleID VARCHAR(50) PRIMARY KEY,"+
                        "ReportCode VARCHAR(20),"+
                        "ReportName VARCHAR(50),"+
                        "ReportDay DATETIME,"+
                        "Longitude FLOAT," +
                        "Latitude FLOAT," +
                        "LocationAddress VARCHAR(255)," +
                        "ReceiverList VARCHAR(255),"+
                        "Notes VARCHAR(50),"+
                        "IsStatus INTEGER,"+
                        "IsPost BIT,"+
                        "PostDay DATETIME)";
                db.execSQL(sqlCMM);
                //THỊ TRƯỜNG
                sqlCMM="CREATE TABLE SM_REPORT_SALEREP_MARKET(MarketID VARCHAR(50) PRIMARY KEY,"+
                        "ReportSaleID VARCHAR(50),"+
                        "CustomerID VARCHAR(50),"+
                        "CompanyName VARCHAR(100)," +
                        "ProductCode VARCHAR(20),"+
                        "Notes VARCHAR(200),"+
                        "Price FLOAT)";
                db.execSQL(sqlCMM);
                //DỊCH BỆNH
                sqlCMM="CREATE TABLE SM_REPORT_SALEREP_DISEASE(DiseaseID VARCHAR(50) PRIMARY KEY,"+
                        "ReportSaleID VARCHAR(50),"+
                        "TreeCode VARCHAR(15),"+
                        "Title VARCHAR(50)," +
                        "Acreage FLOAT,"+
                        "Notes VARCHAR(200))";
                db.execSQL(sqlCMM);
                //MÙA VỤ
                sqlCMM="CREATE TABLE SM_REPORT_SALEREP_SEASON(SeasonID VARCHAR(50) PRIMARY KEY,"+
                        "ReportSaleID VARCHAR(50),"+
                        "TreeCode VARCHAR(15),"+
                        "SeasonCode VARCHAR(15),"+
                        "Title VARCHAR(50)," +
                        "Acreage FLOAT,"+
                        "Notes VARCHAR(200))";
                db.execSQL(sqlCMM);
                //HOAT ĐỘNG - CÔNG VIỆC - LỊCH CÔNG TÁC: iStYPE: 0 : Công việc, 1: Lịch công tác
                sqlCMM="CREATE TABLE SM_REPORT_SALEREP_ACTIVITIE(ActivitieID VARCHAR(50) PRIMARY KEY,"+
                        "ReportSaleID VARCHAR(50),"+
                        "IsType VARCHAR(15),"+
                        "Workday VARCHAR(15),"+
                        "Title VARCHAR(50)," +
                        "Place VARCHAR(100)," +
                        "Notes VARCHAR(200))";
                db.execSQL(sqlCMM);
                //db.execSQL("ALTER TABLE QR_QRSCAN ADD COLUMN ScanSupportID VARCHAR(50) DEFAULT ''");
                //onCreate(db);

                db.execSQL("ALTER TABLE SM_ORDER_DETAIL ADD COLUMN Notes2 VARCHAR(255) DEFAULT ''");
                db.execSQL("ALTER TABLE SM_ORDER ADD COLUMN IsSample  BIT");

                // KE HOACH BAN HANG
                sqlCMM = "CREATE TABLE SM_PLAN_SALE(PlanID VARCHAR(50) PRIMARY KEY," +
                        "PlanCode NVARCHAR(20)," +
                        "PlanDay DATETIME," +
                        "StartDay DATETIME," +
                        "EndDay DATETIME," +
                        "PlanName NVARCHAR(50)," +
                        "PostDay DATETIME," +
                        "IsPost BIT," +
                        "IsStatus INTEGER," +
                        "Notes NVARCHAR(50))";
                db.execSQL(sqlCMM);

                sqlCMM = "CREATE TABLE SM_PLAN_SALE_DETAIL(PlanDetailID VARCHAR(50) PRIMARY KEY," +
                        "PlanID NVARCHAR(50)," +
                        "CustomerID NVARCHAR(50)," +
                        "ProductCode NVARCHAR(20)," +
                        "AmountBox FLOAT," +
                        "Amount FLOAT," +
                        "Notes NVARCHAR(255)," +
                        "Notes2 NVARCHAR(255))";
                db.execSQL(sqlCMM);

            }
        }catch (Exception ex){
            //Toast.makeText(mCxt, "Không thể nâng cấp DB lên V12."+ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public boolean onClearHIS() {
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("DELETE FROM DM_CUSTOMER");
            db.execSQL("DELETE FROM DM_PROVINCE");
            db.execSQL("DELETE FROM DM_DISTRICT");
            db.execSQL("DELETE FROM DM_WARD");
            db.execSQL("DELETE FROM DM_PRODUCT");
            db.execSQL("DELETE FROM DM_EMPLOYEE");
            db.execSQL("DELETE FROM QR_QRSCAN");
            db.execSQL("DELETE FROM SM_VISITCARD");
            //Toast.makeText(mCxt, "Đã xóa dữ liệu thành công..", Toast.LENGTH_SHORT).show();
            return true;
        }catch (Exception ex){return false;}
    }

    public void addTrace(SQLiteDatabase db){
        // SQLiteDatabase db=getWritableDatabase();
        ContentValues value=new ContentValues();
        value.put("id", 1);
        value.put("trace", 0);
        db.insert("TRACE", null, value);
    }

    //HT_DUNGCHUNG
    public int getRowCount(String mSql){
        try{
            SQLiteDatabase db=getReadableDatabase();
            Cursor oCurs=db.rawQuery(mSql, null);
            //Log.d("Cursor_row",oCurs.getString(0));
            int iSq= oCurs.getCount();
            oCurs.close();
            return  iSq;
        }catch (Exception ex){Log.d("RawQuery:",ex.getMessage());}
        return  0;
    }

    //[HT_PARA]
    public void addHTPara(HT_PARA oHTPara){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("PARA_NAME", oHTPara.getParaCode());
            values.put("PARA_VAL", oHTPara.getParaValue());
            if (getSizePara(oHTPara.getParaCode())>0){
                values.put("PARA_VAL",oHTPara.getParaValue());
                db.update("HT_PARA",values,"PARA_NAME" +"=?",new String[] { String.valueOf(oHTPara.getParaCode())});
            }else{
                db.insert("HT_PARA", null, values);
            }
            Log.d("PARA_ADD", oHTPara.getParaValue());
            db.close();
        }catch (Exception ex){
            Log.d("INS_HT_PARA",ex.getMessage().toString());}
    }
    public void editHTParam(String ParamName, String ParaVal){
        try {
            SQLiteDatabase db = getWritableDatabase();
            String mSql = String.format("update HT_PARA set PARA_VAL='%s' where PARA_NAME='%s'", ParamName, ParaVal);
            db.execSQL(mSql);
        }catch (Exception ex){}
    }

    public int getSizePara(String ParaName){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM HT_PARA WHERE PARA_NAME=?", new String[]{ParaName});
            int iSq= cursor.getCount();
            cursor.close();
            return  iSq;
        }catch(Exception ex){return 0;}
    }
    public String getParam(String ParamName){
        try {
            SQLiteDatabase db = getReadableDatabase();
            String mSql = String.format("select * from HT_PARA where PARA_NAME='%s'", ParamName);
            Cursor cursor = db.rawQuery(mSql, null);
            String value="";
            if(cursor.moveToFirst()){
                value=cursor.getString(cursor.getColumnIndex("PARA_VAL"));
            }
            return value;
        }catch (Exception ex){return "";}
    }

    public List<HT_PARA> getAllPara() {
        List<HT_PARA> lstPara = new ArrayList<HT_PARA>();

        String selectQuery = "SELECT  * FROM HT_PARA ORDER BY PARA_NAME ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HT_PARA oPara = new HT_PARA();
                oPara.setParaCode(cursor.getString(0));
                oPara.setParaValue(cursor.getString(1));

                lstPara.add(oPara);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lstPara;
    }


    //QR-SCAN
    public int getSizeQRScan(String QRID,String Customerid){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM QR_QRSCAN WHERE Qrid=? AND Customerid=?", new String[]{QRID,Customerid});
            int iSq= cursor.getCount();
            cursor.close();
            return  iSq;
        }catch(Exception ex){return 0;}
    }

    public int getQrScanNo(String QRID,String Customerid){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT COALESCE(max(ScanNo) ,0) FROM QR_QRSCAN WHERE Qrid=? AND Customerid=?", new String[]{QRID,Customerid});
            if (cursor.moveToFirst()) {
                do {
                    return cursor.getInt(0);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }catch(Exception ex){return 0;}
        return 0;
    }

    public boolean addQRScan(QR_QRSCAN oQR){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int iSq = 1;

            //iSq=getSizeQRScan(oQR.getQrid(),oQR.getCustomerid());
            iSq=getQrScanNo(oQR.getQrid(),oQR.getCustomerid());
            if (iSq<=0) {
                ContentValues values = new ContentValues();
                values.put("Qrscanid", oQR.getQrscanid());
                values.put("Customerid", oQR.getCustomerid());
                values.put("CommandNo", oQR.getCommandNo());
                values.put("Qrid", oQR.getQrid());
                values.put("ProductCode", oQR.getProductCode());
                values.put("ProductName", oQR.getProductName());
                values.put("Unit", oQR.getUnit());
                values.put("Specification", oQR.getSpecification());
                values.put("ScanNo", oQR.getScanNo());
                values.put("ScanDay", oQR.getScanDay());
                values.put("Longitude", oQR.getLongitude());
                values.put("Latitude", oQR.getLatitude());
                values.put("LocationAddress", oQR.getLocationAddress());
                values.put("ScanSupportID",oQR.getScanSupportID());
                values.put("Imei", oQR.getImei());
                values.put("ImeiSim", oQR.getImeiSim());
                values.put("SyncDay", oQR.getScanDay());
                values.put("IsSync", oQR.getSync());
                values.put("ScanType",oQR.getScanType());
                db.insert("QR_QRSCAN", null, values);
            }else{
                //String mSql = String.format("update QR_QRSCAN set PARA_VAL='%s' where PARA_NAME='%s'", ParamName, ParaVal);
                //db.execSQL(mSql);
                iSq=iSq+1;
                ContentValues values = new ContentValues();
                values.put("CommandNo", oQR.getCommandNo());
                values.put("ProductCode", oQR.getProductCode());
                values.put("ProductName", oQR.getProductName());
                values.put("Unit", oQR.getUnit());
                values.put("Specification", oQR.getSpecification());
                values.put("ScanNo", iSq);
                values.put("ScanDay", oQR.getScanDay());
                values.put("Longitude", oQR.getLongitude());
                values.put("Latitude", oQR.getLatitude());
                values.put("LocationAddress", oQR.getLocationAddress());
                values.put("ScanSupportID",oQR.getScanSupportID());
                values.put("Imei", oQR.getImei());
                values.put("ImeiSim", oQR.getImeiSim());
                values.put("SyncDay", oQR.getScanDay());
                values.put("IsSync", oQR.getSync());
                values.put("ScanType",oQR.getScanType());
                db.update("QR_QRSCAN",values,"Qrid=? AND Customerid=?" ,new String[] { String.valueOf(oQR.getQrid()),String.valueOf(oQR.getCustomerid())});
            }

            Log.d("INS_QRSCAN_DB","OK");
            db.close();

            return true;
        }catch (Exception e){Log.v("INS_QRSCAN_ERR",e.getMessage()); return  false;}
    }



    public boolean editQRScanStatus(QR_QRSCAN oQR){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("SyncDay", oQR.getScanDay());
            values.put("IsSync", oQR.getSync());
            db.update("QR_QRSCAN",values,"Qrid=? AND Customerid=?" ,new String[] { String.valueOf(oQR.getQrid()),String.valueOf(oQR.getCustomerid())});

            db.close();
            return true;
        }catch (Exception e){Log.v("INS_QRSCAN_ERR",e.getMessage()); return  false;}
    }

    public boolean delQRScan(QR_QRSCAN oQr){
        try {
            SQLiteDatabase db = getWritableDatabase();
            String mSql=String.format("delete from QR_QRSCAN where Qrscanid ='%s'",oQr.getQrscanid());
            try {
                db.execSQL(mSql);
                //Log.d("DEL_QRSCAN",oQr.getQrscanid());
            }catch (Exception ex){
                Log.d("DEL_QRSCAN",ex.getMessage());
                return  false;
            }
            return  true;
            //db.delete("QR_QRSCAN", "malt = ?", new String[]{malt});
        }catch (Exception ex){return false;}
    }


    public List<QR_QRSCAN> getAllQRScan(String fDay,String tDay) {
        List<QR_QRSCAN> lstPara = new ArrayList<QR_QRSCAN>();
        String selectQuery =String.format("SELECT  A.*,B.CustomerName,C.EmployeeName FROM QR_QRSCAN A LEFT JOIN DM_CUSTOMER B ON A.Customerid=B.Customerid LEFT JOIN DM_EMPLOYEE C ON A.ScanSupportID=C.Employeeid where (julianday(A.ScanDay)-julianday('%s')) >=0 and (julianday('%s')-julianday(A.ScanDay)) >=0 ORDER BY ScanDay desc,ProductCode asc",fDay,tDay);
        //String selectQuery =String.format("SELECT  A.*,B.CustomerName FROM QR_QRSCAN A LEFT JOIN DM_CUSTOMER B ON A.Customerid=B.Customerid  ORDER BY ScanDay desc,ProductCode asc");
        //Log.d("SQL",selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                QR_QRSCAN oPara = new QR_QRSCAN();
                oPara.setQrscanid(cursor.getString(cursor.getColumnIndex("Qrscanid")));
                oPara.setCustomerid(cursor.getString(cursor.getColumnIndex("Customerid")));
                oPara.setCommandNo(cursor.getString(cursor.getColumnIndex("CommandNo")));
                oPara.setQrid(cursor.getString(cursor.getColumnIndex("Qrid")));
                oPara.setProductCode(cursor.getString(cursor.getColumnIndex("ProductCode")));
                oPara.setProductName(cursor.getString(cursor.getColumnIndex("ProductName")));
                oPara.setUnit(cursor.getString(cursor.getColumnIndex("Unit")));
                oPara.setSpecification(cursor.getString(cursor.getColumnIndex("Specification")));
                oPara.setScanNo(Integer.valueOf(cursor.getString(cursor.getColumnIndex("ScanNo"))));
                oPara.setScanDay(cursor.getString(cursor.getColumnIndex("ScanDay")));
                oPara.setLongitude(Double.valueOf(cursor.getString(cursor.getColumnIndex("Longitude"))));
                oPara.setLatitude(Double.valueOf(cursor.getString(cursor.getColumnIndex("Latitude"))));
                oPara.setLocationAddress(cursor.getString(cursor.getColumnIndex("LocationAddress")));
                oPara.setScanSupportID(cursor.getString(cursor.getColumnIndex("ScanSupportID")));
                oPara.setScanSupportDesc(cursor.getString(cursor.getColumnIndex("EmployeeName")));
                oPara.setImei(cursor.getString(cursor.getColumnIndex("Imei")));
                oPara.setImeiSim(cursor.getString(cursor.getColumnIndex("ImeiSim")));
                oPara.setSyncDay(cursor.getString(cursor.getColumnIndex("SyncDay")));
                if(cursor.getString(cursor.getColumnIndex("IsSync")).contains("1")){
                    oPara.setSync(true);
                }else{
                    oPara.setSync(false);
                }
                oPara.setScanType(cursor.getString(cursor.getColumnIndex("ScanType")));

                oPara.setCustomerName(cursor.getString(cursor.getColumnIndex("CustomerName")));

                lstPara.add(oPara);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lstPara;
    }


    public List<QR_QRSCAN> getQRScan(String QrID,String CustomerID) {
        List<QR_QRSCAN> lstPara = new ArrayList<QR_QRSCAN>();
        String selectQuery = "SELECT  * FROM QR_QRSCAN where Qrid=? and Customerid=? ORDER BY ScanDay desc,ProductCode asc";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{QrID,CustomerID});

        if (cursor.moveToFirst()) {
            do {
                QR_QRSCAN oPara = new QR_QRSCAN();
                oPara.setQrscanid(cursor.getString(cursor.getColumnIndex("Qrscanid")));
                oPara.setCustomerid(cursor.getString(cursor.getColumnIndex("Customerid")));
                oPara.setCommandNo(cursor.getString(cursor.getColumnIndex("CommandNo")));
                oPara.setQrid(cursor.getString(cursor.getColumnIndex("Qrid")));
                oPara.setProductCode(cursor.getString(cursor.getColumnIndex("ProductCode")));
                oPara.setProductName(cursor.getString(cursor.getColumnIndex("ProductName")));
                oPara.setUnit(cursor.getString(cursor.getColumnIndex("Unit")));
                oPara.setSpecification(cursor.getString(cursor.getColumnIndex("Specification")));
                oPara.setScanNo(Integer.valueOf(cursor.getString(cursor.getColumnIndex("ScanNo"))));
                oPara.setScanDay(cursor.getString(cursor.getColumnIndex("ScanDay")));
                oPara.setLongitude(Double.valueOf(cursor.getString(cursor.getColumnIndex("Longitude"))));
                oPara.setLatitude(Double.valueOf(cursor.getString(cursor.getColumnIndex("Latitude"))));
                oPara.setLocationAddress(cursor.getString(cursor.getColumnIndex("LocationAddress")));
                oPara.setScanSupportID(cursor.getString(cursor.getColumnIndex("ScanSupportID")));
                oPara.setScanSupportDesc(cursor.getString(cursor.getColumnIndex("EmployeeName")));
                oPara.setImei(cursor.getString(cursor.getColumnIndex("Imei")));
                oPara.setImeiSim(cursor.getString(cursor.getColumnIndex("ImeiSim")));
                oPara.setSyncDay(cursor.getString(cursor.getColumnIndex("SyncDay")));
                if(cursor.getString(cursor.getColumnIndex("IsSync")).contains("1")){
                    oPara.setSync(true);
                }else{
                    oPara.setSync(false);
                }
                oPara.setScanType(cursor.getString(cursor.getColumnIndex("ScanType")));
                //oPara.setSync(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("IsSync"))));

                lstPara.add(oPara);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lstPara;
    }


    //DM_PRODUCT
    public int getSizeProduct(String ProductCode){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM DM_PRODUCT WHERE ProductCode=?",new String[]{ProductCode});
            int iSq= cursor.getCount();
            cursor.close();
            return  iSq;
        }catch(Exception ex){return 0;}
    }

    public boolean addProduct(DM_Product oPro){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int iSq = 1;

            iSq=getSizeProduct(oPro.getProductCode().toString());
            if (iSq<=0) {
                ContentValues values = new ContentValues();
                values.put("ProductCode",oPro.getProductCode());
                values.put("ProductName", oPro.getProductName());
                values.put("Unit",oPro.getUnit());
                values.put("Spec", oPro.getSpecification());
                values.put("ConvertKgl", oPro.getConvertKgl());
                values.put("ConvertBox", oPro.getConvertBox());
                values.put("isMain", oPro.getMain());
                db.insert("DM_PRODUCT", null, values);
            }else{
                //String mSql = String.format("update QR_QRSCAN set PARA_VAL='%s' where PARA_NAME='%s'", ParamName, ParaVal);
                //db.execSQL(mSql);
                iSq=iSq+1;
                ContentValues values = new ContentValues();
                values.put("ProductName", oPro.getProductName());
                values.put("Unit",oPro.getUnit());
                values.put("Spec", oPro.getSpecification());
                values.put("ConvertKgl", oPro.getConvertKgl());
                values.put("ConvertBox", oPro.getConvertBox());
                values.put("isMain", oPro.getMain());
                db.update("DM_PRODUCT",values,"ProductCode=?" ,new String[] { String.valueOf(oPro.getProductCode())});
            }

            //Log.d("INS_PRODUCT_DB","OK");
            db.close();
            return true;
        }catch (Exception e){Log.v("INS_PRODUCT_ERR",e.getMessage()); return  false;}
    }

    public List<DM_Product> getAllProduct() {
        List<DM_Product> lstPara = new ArrayList<DM_Product>();
        String selectQuery = "SELECT ProductCode,ProductGroupID,ProductName,Unit," +
                "Spec,ConvertKgl,ConvertBox,isMain FROM DM_PRODUCT ORDER BY ProductCode ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                DM_Product oPara = new DM_Product();
                oPara.setProductCode(cursor.getString(cursor.getColumnIndex("ProductCode")));
                oPara.setProductName(cursor.getString(cursor.getColumnIndex("ProductName")));
                oPara.setUnit(cursor.getString(cursor.getColumnIndex("Unit")));
                oPara.setSpecification(cursor.getString(cursor.getColumnIndex("Spec")));
                oPara.setConvertKgl(cursor.getFloat(cursor.getColumnIndex("ConvertKgl")));
                oPara.setConvertBox(cursor.getFloat(cursor.getColumnIndex("ConvertBox")));
                oPara.setMain(Boolean.getBoolean(cursor.getString(cursor.getColumnIndex("isMain"))));
                lstPara.add(oPara);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lstPara;
    }


    public DM_Product getProduct(String mProductCode) {
        List<DM_Product> lstPara = new ArrayList<DM_Product>();
        String selectQuery = "SELECT  ProductCode,ProductGroupID,ProductName,Unit," +
                "Spec,ConvertKgl,ConvertBox,isMain FROM DM_PRODUCT WHERE ProductCode=? ORDER BY ProductCode ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{mProductCode});
        DM_Product oPara = new DM_Product();

        if (cursor.moveToFirst()) {
            do {
                oPara.setProductCode(cursor.getString(cursor.getColumnIndex("ProductCode")));
                oPara.setProductName(cursor.getString(cursor.getColumnIndex("ProductName")));
                oPara.setSpecification(cursor.getString(cursor.getColumnIndex("Spec")));
                oPara.setConvertKgl(cursor.getFloat(cursor.getColumnIndex("ConvertKgl")));
                oPara.setConvertBox(cursor.getFloat(cursor.getColumnIndex("ConvertBox")));
                oPara.setUnit(cursor.getString(cursor.getColumnIndex("Unit")));
                oPara.setMain(Boolean.getBoolean(cursor.getString(cursor.getColumnIndex("isMain"))));

                cursor.close();
                db.close();
                return oPara;
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return null ;
    }

    public boolean delProduct(String ProductCode,boolean isAll){
        try {
            String mSql;
            SQLiteDatabase db = getWritableDatabase();
            if(isAll) {
                mSql = String.format("delete from DM_PRODUCT");
            }else{
                mSql = String.format("delete from DM_PRODUCT where ProductCode='%s'", ProductCode);
            }
            try {
                db.execSQL(mSql);
            }catch (Exception ex){
                return  false;
            }
            return  true;
        }catch (Exception ex){return false;}
    }


    //DM_PROVINCE
    public int getSizeProvince(String Provinceid){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM DM_PROVINCE WHERE Provinceid=?",new String[]{Provinceid});
            int iSq= cursor.getCount();
            cursor.close();
            return  iSq;
        }catch(Exception ex){return 0;}
    }

    public boolean addProvince(DM_Province oPro){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int iSq = 1;

            iSq=getSizeProvince(oPro.getProvinceid().toString());
            if (iSq<=0) {
                ContentValues values = new ContentValues();
                values.put("Provinceid",oPro.getProvinceid());
                values.put("ProvinceCode", oPro.getProvinceCode());
                values.put("Province",oPro.getProvince());
                values.put("ZoneCode", oPro.getZoneCode());
                db.insert("DM_PROVINCE", null, values);
            }else{
                //String mSql = String.format("update QR_QRSCAN set PARA_VAL='%s' where PARA_NAME='%s'", ParamName, ParaVal);
                //db.execSQL(mSql);
                iSq=iSq+1;
                ContentValues values = new ContentValues();
                values.put("ProvinceCode", oPro.getProvinceCode());
                values.put("Province",oPro.getProvince());
                values.put("ZoneCode", oPro.getZoneCode());
                db.update("DM_PROVINCE",values,"Provinceid=?" ,new String[] { String.valueOf(oPro.getProvinceid().toString())});
            }

            //Log.d("INS_PROVINCE_DB","OK");
            db.close();

            return true;
        }catch (Exception e){Log.v("INS_DISTRICT_ERR",e.getMessage()); return  false;}
    }

    public List<DM_Province> getAllProvince() {
        List<DM_Province> lstPara = new ArrayList<DM_Province>();
        String selectQuery = "SELECT  * FROM DM_PROVINCE ORDER BY Province ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DM_Province oPara = new DM_Province();
                oPara.setProvinceid(cursor.getInt(0));
                oPara.setProvinceCode(cursor.getString(1));
                oPara.setProvince(cursor.getString(2));
                lstPara.add(oPara);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lstPara;
    }

    public boolean delProvince(String Provinceid,boolean isAll){
        try {
            String mSql;
            SQLiteDatabase db = getWritableDatabase();
            if(isAll) {
                mSql = String.format("delete from DM_PROVINCE");
            }else{
                mSql = String.format("delete from DM_PROVINCE where Provinceid='%s'", Provinceid);
            }
            try {
                db.execSQL(mSql);
            }catch (Exception ex){
                return  false;
            }
            return  true;
        }catch (Exception ex){return false;}
    }


    //DM_DISTRICT
    public int getSizeDistrict(String Districtid){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM DM_DISTRICT WHERE Districtid=?",new String[]{Districtid});
            int iSq= cursor.getCount();
            cursor.close();
            return  iSq;
        }catch(Exception ex){return 0;}
    }

    public boolean addDistrict(DM_District oPro){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int iSq = 1;

            iSq=getSizeDistrict(Integer.toString(oPro.getDistrictid()));
            if (iSq<=0) {
                ContentValues values = new ContentValues();
                values.put("Districtid",oPro.getDistrictid());
                values.put("Provinceid",oPro.getProvinceid());
                values.put("District", oPro.getDistrict());
                db.insert("DM_DISTRICT", null, values);
            }else{
                iSq=iSq+1;
                ContentValues values = new ContentValues();
                values.put("District", oPro.getDistrict());
                values.put("Provinceid",oPro.getProvinceid());
                db.update("DM_DISTRICT",values,"Districtid=?" ,new String[] { String.valueOf(oPro.getDistrictid())});
            }

            //Log.d("INS_DISTRICT_DB","OK");
            db.close();

            return true;
        }catch (Exception e){Log.v("INS_DISTRICT_ERR",e.getMessage()); return  false;}
    }

    public List<DM_District> getAllDistrict() {
        List<DM_District> lstPara = new ArrayList<DM_District>();

        String selectQuery = "SELECT  A.Districtid,A.Provinceid,A.District,B.Province FROM DM_DISTRICT A LEFT JOIN DM_PROVINCE B ON A.Provinceid=b.Provinceid ORDER BY District ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DM_District oPara = new DM_District();
                oPara.setDistrictid(cursor.getInt(cursor.getColumnIndex("Districtid")));
                oPara.setProvinceid(cursor.getInt(cursor.getColumnIndex("Provinceid")));
                oPara.setDistrict(cursor.getString(cursor.getColumnIndex("District")));
                oPara.setProvince(cursor.getString(cursor.getColumnIndex("Province")));

                lstPara.add(oPara);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();


        return lstPara;
    }
    public List<DM_District> getDistrict(Integer Provinceid) {
        List<DM_District> lstPara = new ArrayList<DM_District>();

        String selectQuery = String.format("SELECT  A.Districtid,A.Provinceid,A.District,B.Province FROM DM_DISTRICT A LEFT JOIN DM_PROVINCE B ON A.Provinceid=b.Provinceid where A.Provinceid='%d' ORDER BY District ASC",Provinceid);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //Log.d("SQL_SEL_DISTRICT",selectQuery);
        if (cursor.moveToFirst()) {
            do {
                DM_District oPara = new DM_District();
                oPara.setDistrictid(cursor.getInt(cursor.getColumnIndex("Districtid")));
                oPara.setProvinceid(cursor.getInt(cursor.getColumnIndex("Provinceid")));
                oPara.setDistrict(cursor.getString(cursor.getColumnIndex("District")));
                oPara.setProvince(cursor.getString(cursor.getColumnIndex("Province")));

                lstPara.add(oPara);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();


        return lstPara;
    }

    public boolean delDistrict(String Districtid,boolean isAll){
        try {
            String mSql;
            SQLiteDatabase db = getWritableDatabase();
            if(isAll) {
                mSql = String.format("delete from DM_DISTRICT");
            }else{
                mSql = String.format("delete from DM_DISTRICT where Districtid='%s'", Districtid);
            }
            try {
                db.execSQL(mSql);
            }catch (Exception ex){
                return  false;
            }
            return  true;
        }catch (Exception ex){return false;}
    }

    //DM_WARD
    public int getSizeWard(String Wardid){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM DM_WARD WHERE Wardid=?",new String[]{Wardid});
            int iSq= cursor.getCount();
            cursor.close();
            return  iSq;
        }catch(Exception ex){return 0;}
    }

    public boolean addWard(DM_Ward oPro){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int iSq = 1;

            iSq=getSizeWard(Integer.toString(oPro.getWardid()));
            if (iSq<=0) {
                ContentValues values = new ContentValues();
                values.put("Wardid",oPro.getWardid());
                values.put("Districtid",oPro.getDistrictid());
                values.put("Ward", oPro.getWard());
                db.insert("DM_WARD", null, values);
            }else{
                iSq=iSq+1;
                ContentValues values = new ContentValues();
                values.put("Ward", oPro.getWard());
                values.put("Wardid",oPro.getWardid());
                db.update("DM_WARD",values,"Wardid=?" ,new String[] { String.valueOf(oPro.getWardid())});
            }

            //Log.d("INS_WARD_DB","OK");
            db.close();

            return true;
        }catch (Exception e){Log.v("INS_WARD_ERR",e.getMessage()); return  false;}
    }

    public List<DM_Ward> getAllWard() {
        List<DM_Ward> lstPara = new ArrayList<DM_Ward>();

        String selectQuery = "SELECT  A.*,B.District FROM DM_WARD A LEFT JOIN DM_DISTRICT B ON A.Districtid=b.Districtid ORDER BY Ward ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DM_Ward oPara = new DM_Ward();
                oPara.setWardid(cursor.getInt(0));
                oPara.setDistrictid(cursor.getInt(1));
                oPara.setWard(cursor.getString(2));
                oPara.setDistrict(cursor.getString(4));

                lstPara.add(oPara);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lstPara;
    }


    public List<DM_Ward> getWard(Integer Districtid) {
        List<DM_Ward> lstPara = new ArrayList<DM_Ward>();

        String selectQuery =String.format("SELECT  A.*,B.District FROM DM_WARD A LEFT JOIN DM_DISTRICT B ON A.Districtid=b.Districtid where A.Districtid='%d' ORDER BY Ward ASC",Districtid);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DM_Ward oPara = new DM_Ward();
                oPara.setWardid(cursor.getInt(0));
                oPara.setDistrictid(cursor.getInt(1));
                oPara.setWard(cursor.getString(2));
                oPara.setDistrict(cursor.getString(4));

                lstPara.add(oPara);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lstPara;
    }

    public boolean delWard(String Wardid,boolean isAll){
        try {
            String mSql;
            SQLiteDatabase db = getWritableDatabase();
            if(isAll) {
                mSql = String.format("delete from DM_WARD");
            }else{
                mSql = String.format("delete from DM_WARD where Wardid='%s'", Wardid);
            }
            try {
                db.execSQL(mSql);
            }catch (Exception ex){
                return  false;
            }
            return  true;
        }catch (Exception ex){return false;}
    }


    //CUSTOMER
    public int getSizeCustomerPenddingAprrove(){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT Customerid FROM DM_CUSTOMER WHERE EditStatus =? OR EditStatus=?",new String[]{"4","6"});
            int iSq= cursor.getCount();
            cursor.close();
            return  iSq;
        }catch(Exception ex){return -1;}
    }

    public int getSizeCustomer(String CustomerID){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM DM_CUSTOMER WHERE Customerid=?", new String[]{CustomerID});
            int iSq= cursor.getCount();
            cursor.close();
            return  iSq;
        }catch(Exception ex){return -1;}
    }

    public DM_Customer getCustomer(String CustomerID) {
        try {
            DM_Customer oCus = new DM_Customer();

            String selectQuery = "SELECT  A.Customerid,A.Employeeid,A.CustomerCode,A.CustomerName," +
                    " A.ShortName,A.Represent,A.Provinceid,A.Districtid,A.Wardid,A.Street,A.Tel,A.Tax,A.Fax,A.Email," +
                    " A.Longitude,A.Latitude,A.LongitudeTemp,A.LatitudeTemp,A.LocationAddressTemp,A.EditStatus,A.Notes,A.Ranked, " +
                    " B.Province,C.District,D.Ward " +
                    " FROM DM_CUSTOMER A LEFT JOIN DM_PROVINCE B ON A.Provinceid=b.Provinceid " +
                    " LEFT JOIN DM_DISTRICT C ON A.Districtid=C.Districtid "+
                    " LEFT JOIN DM_WARD D ON A.Wardid=D.Wardid "+
                    " WHERE A.Customerid=? ORDER BY Customerid ASC";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, new String[]{CustomerID});

            if (cursor.moveToFirst()) {
                do {
                    oCus.setCustomerid(cursor.getString(cursor.getColumnIndex("Customerid")));
                    oCus.setEmployeeid(cursor.getString(cursor.getColumnIndex("Employeeid")));
                    oCus.setCustomerCode(cursor.getString(cursor.getColumnIndex("CustomerCode")));

                    oCus.setCustomerName(cursor.getString(cursor.getColumnIndex("CustomerName")));
                    oCus.setShortName(cursor.getString(cursor.getColumnIndex("ShortName")));
                    oCus.setRepresent(cursor.getString(cursor.getColumnIndex("Represent")));
                    oCus.setProvinceid(cursor.getInt(cursor.getColumnIndex("Provinceid")));
                    oCus.setDistrictid(cursor.getInt(cursor.getColumnIndex("Districtid")));
                    oCus.setWardid(cursor.getInt(cursor.getColumnIndex("Wardid")));
                    oCus.setStreet(cursor.getString(cursor.getColumnIndex("Street")));
                    oCus.setTel(cursor.getString(cursor.getColumnIndex("Tel")));
                    oCus.setTax(cursor.getString(cursor.getColumnIndex("Tax")));
                    oCus.setFax(cursor.getString(cursor.getColumnIndex("Fax")));
                    oCus.setEmail(cursor.getString(cursor.getColumnIndex("Email")));
                    oCus.setLongitude(cursor.getDouble(cursor.getColumnIndex("Longitude")));
                    oCus.setLatitude(cursor.getDouble(cursor.getColumnIndex("Latitude")));
                    oCus.setLongitudeTemp(cursor.getDouble(cursor.getColumnIndex("LongitudeTemp")));
                    oCus.setLatitudeTemp(cursor.getDouble(cursor.getColumnIndex("LatitudeTemp")));
                    oCus.setLocationAddressTemp(cursor.getString(cursor.getColumnIndex("LocationAddressTemp")));
                    oCus.setEdit(cursor.getInt(cursor.getColumnIndex("EditStatus")));
                    if(cursor.getInt(cursor.getColumnIndex("EditStatus"))==1){
                        oCus.setStatusDesc("Mới");
                    }else if(cursor.getInt(cursor.getColumnIndex("EditStatus"))==2){
                        oCus.setStatusDesc("Sửa");
                    }else if(cursor.getInt(cursor.getColumnIndex("EditStatus"))==3){
                        oCus.setStatusDesc("Xóa");
                    }else if(cursor.getInt(cursor.getColumnIndex("EditStatus"))==4){
                        oCus.setStatusDesc("Chờ duyệt");
                    }else if(cursor.getInt(cursor.getColumnIndex("EditStatus"))==5){
                        oCus.setStatusDesc("Từ chối");
                    }else{
                        oCus.setStatusDesc("");
                    }
                    oCus.setProvinceName(cursor.getString(cursor.getColumnIndex("Province")));
                    oCus.setDistrictName(cursor.getString(cursor.getColumnIndex("District")));
                    oCus.setWardName(cursor.getString(cursor.getColumnIndex("Ward")));
                    oCus.setNotes(cursor.getString(cursor.getColumnIndex("Notes")));
                    oCus.setRanked(cursor.getString(cursor.getColumnIndex("Ranked")));

                    return  oCus;
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return oCus;
        }catch (Exception ex){Log.d("ERR_LOAD_CUS",ex.getMessage().toString());}
        return null;
    }

    public List<DM_Customer> getAllCustomer() {
        try {
            List<DM_Customer> lstPara = new ArrayList<DM_Customer>();

            String selectQuery = "SELECT  A.Customerid,A.Employeeid,A.CustomerCode,A.CustomerName," +
                    " A.ShortName,A.Represent,A.Provinceid,A.Districtid,A.Wardid,A.Street,A.Tel,A.Tax,A.Fax,A.Email," +
                    " A.Longitude,A.Latitude,A.LongitudeTemp,A.LatitudeTemp,A.LocationAddress,A.LocationAddressTemp,A.EditStatus,A.Notes,A.Ranked, " +
                    " B.Province,C.District,D.Ward " +
                    " FROM DM_CUSTOMER A LEFT JOIN DM_PROVINCE B ON A.Provinceid=b.Provinceid " +
                    " LEFT JOIN DM_DISTRICT C ON A.Districtid=C.Districtid "+
                    " LEFT JOIN DM_WARD D ON A.Wardid=D.Wardid "+
                    " ORDER BY A.EditStatus desc, Customerid ASC";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    DM_Customer oCus = new DM_Customer();
                    oCus.setCustomerid(cursor.getString(cursor.getColumnIndex("Customerid")));
                    oCus.setEmployeeid(cursor.getString(cursor.getColumnIndex("Employeeid")));
                    oCus.setCustomerCode(cursor.getString(cursor.getColumnIndex("CustomerCode")));

                    oCus.setCustomerName(cursor.getString(cursor.getColumnIndex("CustomerName")));
                    oCus.setShortName(cursor.getString(cursor.getColumnIndex("ShortName")));
                    oCus.setRepresent(cursor.getString(cursor.getColumnIndex("Represent")));
                    oCus.setProvinceid(cursor.getInt(cursor.getColumnIndex("Provinceid")));
                    oCus.setDistrictid(cursor.getInt(cursor.getColumnIndex("Districtid")));
                    oCus.setWardid(cursor.getInt(cursor.getColumnIndex("Wardid")));
                    oCus.setStreet(cursor.getString(cursor.getColumnIndex("Street")));
                    oCus.setTax(cursor.getString(cursor.getColumnIndex("Tax")));
                    oCus.setTel(cursor.getString(cursor.getColumnIndex("Tel")));
                    oCus.setFax(cursor.getString(cursor.getColumnIndex("Fax")));
                    oCus.setEmail(cursor.getString(cursor.getColumnIndex("Email")));
                    oCus.setLongitude(cursor.getDouble(cursor.getColumnIndex("Longitude")));
                    oCus.setLatitude(cursor.getDouble(cursor.getColumnIndex("Latitude")));
                    oCus.setLocationAddress(cursor.getString(cursor.getColumnIndex("LocationAddress")));

                    if(cursor.getDouble(cursor.getColumnIndex("LongitudeTemp"))<=0){
                        oCus.setLongitudeTemp(cursor.getDouble(cursor.getColumnIndex("Longitude")));
                    }else{
                        oCus.setLongitudeTemp(cursor.getDouble(cursor.getColumnIndex("LongitudeTemp")));
                    }

                    if(cursor.getDouble(cursor.getColumnIndex("LatitudeTemp"))<=0) {
                        oCus.setLatitudeTemp(cursor.getDouble(cursor.getColumnIndex("Latitude")));
                    }else{
                        oCus.setLatitudeTemp(cursor.getDouble(cursor.getColumnIndex("LatitudeTemp")));
                    }

                    oCus.setLocationAddressTemp(cursor.getString(cursor.getColumnIndex("LocationAddressTemp")));
                    oCus.setEdit(cursor.getInt(cursor.getColumnIndex("EditStatus")));
                    if(cursor.getInt(cursor.getColumnIndex("EditStatus"))==1){
                        oCus.setStatusDesc("Mới");
                    }else if(cursor.getInt(cursor.getColumnIndex("EditStatus"))==2){
                        oCus.setStatusDesc("Sửa");
                    }else if(cursor.getInt(cursor.getColumnIndex("EditStatus"))==3){
                        oCus.setStatusDesc("Xóa");
                    }else if(cursor.getInt(cursor.getColumnIndex("EditStatus"))==4){
                        oCus.setStatusDesc("Chờ duyệt");
                    }else if(cursor.getInt(cursor.getColumnIndex("EditStatus"))==5){
                        oCus.setStatusDesc("Từ chối");
                    }else{
                        oCus.setStatusDesc("");
                    }

                    oCus.setProvinceName(cursor.getString(cursor.getColumnIndex("Province")));
                    oCus.setDistrictName(cursor.getString(cursor.getColumnIndex("District")));
                    oCus.setWardName(cursor.getString(cursor.getColumnIndex("Ward")));
                    oCus.setNotes(cursor.getString(cursor.getColumnIndex("Notes")));
                    oCus.setRanked(cursor.getString(cursor.getColumnIndex("Ranked")));

                    lstPara.add(oCus);

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return lstPara;
        }catch (Exception ex){Log.d("ERR_LOAD_CUS",ex.getMessage().toString());}
        return null;
    }


    public List<DM_Customer_Search> getAllCustomerSearch() {
        try {
            List<DM_Customer_Search> lstPara = new ArrayList<DM_Customer_Search>();

            String selectQuery = "SELECT  A.Customerid,A.CustomerCode,A.CustomerName," +
                    " A.ShortName,A.Represent,B.Province,A.Longitude,A.Latitude" +
                    " FROM DM_CUSTOMER A LEFT JOIN DM_PROVINCE B ON A.Provinceid=b.Provinceid " +
                    " ORDER BY A.EditStatus desc, Customerid ASC";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    DM_Customer_Search oCus = new DM_Customer_Search();
                    oCus.setCustomerid(cursor.getString(cursor.getColumnIndex("Customerid")));
                    oCus.setCustomerCode(cursor.getString(cursor.getColumnIndex("CustomerCode")));
                    oCus.setCustomerName(cursor.getString(cursor.getColumnIndex("CustomerName")));
                    oCus.setShortName(cursor.getString(cursor.getColumnIndex("ShortName")));
                    oCus.setLongititude(cursor.getDouble(cursor.getColumnIndex("Longitude")));
                    oCus.setLatitude(cursor.getDouble(cursor.getColumnIndex("Latitude")));
                    oCus.setProvinceName(cursor.getString(cursor.getColumnIndex("Province")));
                    lstPara.add(oCus);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return lstPara;
        }catch (Exception ex){Log.d("ERR_LOAD_CUS",ex.getMessage().toString());}
        return null;
    }


    public boolean addCustomer(DM_Customer oCus){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int iSq = 1;

            iSq=getSizeCustomer(oCus.getCustomerid());
            if (iSq<0) {
                //Log.d("INS_CUSTOMER_DB","ERROR :-1");
                db.close();
                //return false;
            }

            if (iSq<=0 && iSq!=-1) {
                ContentValues values = new ContentValues();
                values.put("Customerid", oCus.getCustomerid());
                values.put("Employeeid", oCus.getEmployeeid());
                values.put("CustomerCode", oCus.getCustomerCode());
                values.put("CustomerName",oCus.getCustomerName());
                values.put("ShortName", oCus.getShortName());
                values.put("Represent", oCus.getRepresent());
                values.put("Provinceid", oCus.getProvinceid());
                values.put("Districtid", oCus.getDistrictid());
                values.put("Wardid",oCus.getWardid());
                values.put("Street", oCus.getStreet());
                values.put("Address", oCus.getAddress());
                values.put("Tax", oCus.getTax());
                values.put("Tel", oCus.getTel());
                values.put("Fax", oCus.getFax());
                values.put("Email",oCus.getEmail());
                values.put("Longitude", oCus.getLongitude());
                values.put("Latitude", oCus.getLatitude());
                values.put("LocationAddress",oCus.getLocationAddress());
                values.put("Notes", oCus.getNotes());
                values.put("EditStatus", oCus.getEdit());
                values.put("Ranked",oCus.getRanked());

                db.insert("DM_CUSTOMER", null, values);
            }else{
                //String mSql = String.format("update QR_QRSCAN set PARA_VAL='%s' where PARA_NAME='%s'", ParamName, ParaVal);
                //db.execSQL(mSql);
                ContentValues values = new ContentValues();
                values.put("Employeeid", oCus.getEmployeeid());
                values.put("CustomerCode", oCus.getCustomerCode());
                values.put("CustomerName",oCus.getCustomerName());
                values.put("ShortName", oCus.getShortName());
                values.put("Represent", oCus.getRepresent());
                values.put("Provinceid", oCus.getProvinceid());
                values.put("Districtid", oCus.getDistrictid());
                values.put("Wardid",oCus.getWardid());
                values.put("Street", oCus.getStreet());
                values.put("Address", oCus.getAddress());
                values.put("Tel", oCus.getTel());
                values.put("Tax", oCus.getTax());
                values.put("Fax", oCus.getFax());
                values.put("Email",oCus.getEmail());
                values.put("Longitude", oCus.getLongitude());
                values.put("Latitude", oCus.getLatitude());
                values.put("LocationAddress",oCus.getLocationAddress());
                values.put("Notes", oCus.getNotes());
                values.put("EditStatus", oCus.getEdit());
                values.put("Ranked",oCus.getRanked());

                db.update("DM_CUSTOMER",values,"Customerid=?" ,new String[] {String.valueOf(oCus.getCustomerid())});
            }

            //Log.d("INS_CUSTOMER_DB","OK");
            db.close();

            return true;
        }catch (Exception e){Log.v("INS_CUSTOMER_ERR",e.getMessage()); return  false;}
    }


    public String addCustomer2(DM_Customer oCus) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int iSq = 1;
            iSq = getSizeCustomer(oCus.getCustomerid());
            if (iSq < 0) {
                db.close();
                //return false;
            }

            if (iSq <= 0 && iSq != -1) {
                ContentValues values = new ContentValues();
                values.put("Customerid", oCus.getCustomerid());
                values.put("Employeeid", oCus.getEmployeeid());
                values.put("CustomerCode", oCus.getCustomerCode());
                values.put("CustomerName", oCus.getCustomerName());
                values.put("ShortName", oCus.getShortName());
                values.put("Represent", oCus.getRepresent());
                values.put("Provinceid", oCus.getProvinceid());
                values.put("Districtid", oCus.getDistrictid());
                values.put("Wardid", oCus.getWardid());
                values.put("Street", oCus.getStreet());
                values.put("Address", oCus.getAddress());
                values.put("Tel", oCus.getTel());
                values.put("Tax", oCus.getTax());
                values.put("Fax", oCus.getFax());
                values.put("Email", oCus.getEmail());
                values.put("Longitude", 0);
                values.put("Latitude", 0);
                values.put("LocationAddress", "");
                values.put("LongitudeTemp", oCus.getLongitudeTemp());
                values.put("LatitudeTemp", oCus.getLatitudeTemp());
                values.put("LocationAddressTemp",oCus.getLocationAddressTemp());
                values.put("Notes", oCus.getNotes());
                values.put("EditStatus", oCus.getEdit());

                db.insert("DM_CUSTOMER", null, values);
            }
            return "OK";
        }catch (Exception ex){ return ex.getMessage(); }
    }
    public boolean editCustomer(DM_Customer oCus){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            //String mSql = String.format("update QR_QRSCAN set PARA_VAL='%s' where PARA_NAME='%s'", ParamName, ParaVal);
            //db.execSQL(mSql);
            ContentValues values = new ContentValues();
            values.put("Employeeid", oCus.getEmployeeid());
            values.put("CustomerCode", oCus.getCustomerCode());
            values.put("CustomerName",oCus.getCustomerName());
            values.put("ShortName", oCus.getShortName());
            values.put("Represent", oCus.getRepresent());
            values.put("Provinceid", oCus.getProvinceid());
            values.put("Districtid", oCus.getDistrictid());
            values.put("Wardid",oCus.getWardid());
            values.put("Street", oCus.getStreet());
            values.put("Address", oCus.getAddress());
            values.put("Tel", oCus.getTel());
            values.put("Tax", oCus.getTax());
            values.put("Fax", oCus.getFax());
            values.put("Email",oCus.getEmail());
            values.put("LongitudeTemp", oCus.getLongitudeTemp());
            values.put("LatitudeTemp", oCus.getLatitudeTemp());
            values.put("LocationAddressTemp",oCus.getLocationAddressTemp());
            values.put("Notes", oCus.getNotes());
            values.put("EditStatus", oCus.getEdit());

            db.update("DM_CUSTOMER",values,"Customerid=?" ,new String[] {String.valueOf(oCus.getCustomerid())});

            //Log.d("UDP_CUSTOMER_DB","OK");
            db.close();
            return true;
        }catch (Exception e){Log.v("UDP_CUSTOMER_ERR",e.getMessage()); return  false;}
    }

    public boolean delCustomer(String CustomerID,boolean isAll){
        try {
            String mSql;
            SQLiteDatabase db = getWritableDatabase();
            if(isAll) {
                mSql = String.format("delete from DM_CUSTOMER");
            }else{
                mSql = String.format("delete from DM_CUSTOMER where Customerid='%s'", CustomerID);
            }
            try {
                db.execSQL(mSql);
            }catch (Exception ex){
                return  false;
            }
            return  true;
        }catch (Exception ex){return false;}
    }


    //CUSTOMER_DISTANCE
    public List<DM_Customer_Distance> getAllCustomerDistance() {
        try {
            List<DM_Customer_Distance> lstPara = new ArrayList<DM_Customer_Distance>();
            String selectQuery = "SELECT  distinct Customerid,CustomerName,Longitude,Latitude from DM_CUSTOMER WHERE Longitude>0 AND Latitude>0 ";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    DM_Customer_Distance oCus = new DM_Customer_Distance();
                    oCus.setCustomerid(cursor.getString(cursor.getColumnIndex("Customerid")));
                    oCus.setCustomerName(cursor.getString(cursor.getColumnIndex("CustomerName")));
                    oCus.setLongititude(cursor.getDouble(cursor.getColumnIndex("Longitude")));
                    oCus.setLatitude(cursor.getDouble(cursor.getColumnIndex("Latitude")));
                    oCus.setDistance(Float.valueOf(0));
                    lstPara.add(oCus);

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return lstPara;
        }catch (Exception ex){Log.d("ERR_LOAD_CUS",ex.getMessage().toString());}
        return null;
    }



    //DM_EMPLOYEE
    public int getSizeEmployee(String Employeeid){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM DM_EMPLOYEE WHERE Employeeid=?",new String[]{Employeeid});
            int iSq= cursor.getCount();
            cursor.close();
            return  iSq;
        }catch(Exception ex){return 0;}
    }

    public boolean addEmployee(DM_Employee oEmp){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int iSq = 1;

            iSq=getSizeEmployee(oEmp.getEmployeeid().toString());
            if (iSq<=0) {
                ContentValues values = new ContentValues();
                values.put("Employeeid",oEmp.getEmployeeid());
                values.put("EmployeeCode",oEmp.getEmployeeCode());
                values.put("EmployeeName",oEmp.getEmployeeName());
                values.put("Notes", oEmp.getNotes());
                db.insert("DM_EMPLOYEE", null, values);
            }else{
                //String mSql = String.format("update QR_QRSCAN set PARA_VAL='%s' where PARA_NAME='%s'", ParamName, ParaVal);
                //db.execSQL(mSql);
                iSq=iSq+1;
                ContentValues values = new ContentValues();
                values.put("EmployeeCode",oEmp.getEmployeeCode());
                values.put("EmployeeName",oEmp.getEmployeeName());
                values.put("Notes", oEmp.getNotes());
                db.update("DM_EMPLOYEE",values,"Employeeid=?" ,new String[] { String.valueOf(oEmp.getEmployeeid().toString())});
            }
            //Log.d("INS_PROVINCE_DB","OK");
            db.close();

            return true;
        }catch (Exception e){Log.v("INS_DM_EMPLOYEE_ERR",e.getMessage()); return  false;}
    }

    public List<DM_Employee> getAllEmployee() {
        List<DM_Employee> lstPara = new ArrayList<DM_Employee>();
        String selectQuery = "SELECT  * FROM DM_EMPLOYEE ORDER BY EmployeeName ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DM_Employee oPara = new DM_Employee();
                oPara.setEmployeeid(cursor.getString(cursor.getColumnIndex("Employeeid")));
                oPara.setEmployeeCode(cursor.getString(cursor.getColumnIndex("EmployeeCode")));
                oPara.setEmployeeName(cursor.getString(cursor.getColumnIndex("EmployeeName")));
                oPara.setNotes(cursor.getString(cursor.getColumnIndex("Notes")));
                lstPara.add(oPara);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lstPara;
    }

    public boolean delEmployee(String Employeeid,boolean isAll){
        try {
            String mSql;
            SQLiteDatabase db = getWritableDatabase();
            if(isAll) {
                mSql = String.format("delete from DM_EMPLOYEE");
            }else{
                mSql = String.format("delete from DM_EMPLOYEE where Employeeid='%s'", Employeeid);
            }
            try {
                db.execSQL(mSql);
            }catch (Exception ex){
                return  false;
            }
            return  true;
        }catch (Exception ex){return false;}
    }




    //SMVISIT-CARD
    private String getVisitCardid(String VisitDay,String mCustomerID){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM SM_VISITCARD WHERE (julianday(VisitDay)-julianday('%s')=0) and CustomerID='%s' ", new String[]{VisitDay,mCustomerID});

            String VisitCardID="";
            if (cursor.moveToFirst()) {
                do {
                    VisitCardID=cursor.getString(cursor.getColumnIndex("VisitCardID"));
                    break;
                } while (cursor.moveToNext());
            }
            cursor.close();
            return VisitCardID;
        }catch(Exception ex){return  "";}
    }

    public int getSizeVisitCard(String VisitCardID){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM SM_VISITCARD WHERE VisitCardID=?", new String[]{VisitCardID});
            int iSq= cursor.getCount();
            cursor.close();
            return  iSq;
        }catch(Exception ex){return -1;}
    }

    public void CleanVisitCard(int iIntervalDay) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            String mSql = String.format("delete from SM_VISITCARD where julianday('now')-julianday(VisitDay)>%s", iIntervalDay);
            db.execSQL(mSql);
        } catch (Exception ex) {
        }
    }
    //Fortmat yy
    public List<SM_VisitCard> getAllVisitCard(String fday,String tDay) {
        try {
            List<SM_VisitCard> lstVisit = new ArrayList<SM_VisitCard>();
            String mSql=String.format("Select A.*,B.CustomerName from SM_VISITCARD A LEFT JOIN DM_CUSTOMER B on ifnull(a.CustomerID,'')=b.Customerid "+
                    " where (julianday(A.VisitDay)-julianday('%s')) >=0 and (julianday('%s')-julianday(A.VisitDay)) >=0 order by VisitDay desc,VisitTime desc",fday,tDay);
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            if (cursor.moveToFirst()) {
                do {
                    SM_VisitCard oVisitCard = new SM_VisitCard();
                    oVisitCard.setVisitCardID(cursor.getString(cursor.getColumnIndex("VisitCardID")));
                    oVisitCard.setVisitDay(cursor.getString(cursor.getColumnIndex("VisitDay")));
                    oVisitCard.setVisitType(cursor.getString(cursor.getColumnIndex("VisitType")));

                    oVisitCard.setCustomerName(cursor.getString(cursor.getColumnIndex("CustomerName")));
                    oVisitCard.setCustomerID(cursor.getString(cursor.getColumnIndex("CustomerID")));
                    oVisitCard.setVisitTime(cursor.getString(cursor.getColumnIndex("VisitTime")));
                    oVisitCard.setLongitude(cursor.getDouble(cursor.getColumnIndex("Longitude")));
                    oVisitCard.setLatitude(cursor.getDouble(cursor.getColumnIndex("Latitude")));
                    oVisitCard.setLocationAddress(cursor.getString(cursor.getColumnIndex("LocationAddress")));
                    oVisitCard.setVisitNotes(cursor.getString(cursor.getColumnIndex("VisitNotes")));

                    oVisitCard.setSyncDay(cursor.getString(cursor.getColumnIndex("SyncDay")));
                    if(cursor.getString(cursor.getColumnIndex("IsSync")).contains("1")){
                        oVisitCard.setSync(true);
                    }else{
                        oVisitCard.setSync(false);
                    }
                    lstVisit.add(oVisitCard);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return lstVisit;
        }catch (Exception ex){Log.d("ERR_LOAD_SM_VISIT",ex.getMessage().toString());}
        return null;
    }

    public SM_VisitCard getVisitCard(String VisitCardID) {
        try {
            String mSql=String.format("Select A.*,B.CustomerName from SM_VISITCARD A LEFT JOIN DM_CUSTOMER B on a.CustomerID=b.Customerid "+
                    " where A.VisitCardID='%s' order by VisitDay desc",VisitCardID);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);

            SM_VisitCard oVisitCard = new SM_VisitCard();
            if (cursor.moveToFirst()) {
                do {
                    oVisitCard.setVisitCardID(cursor.getString(cursor.getColumnIndex("VisitCardID")));
                    oVisitCard.setVisitDay(cursor.getString(cursor.getColumnIndex("VisitDay")));
                    oVisitCard.setVisitType(cursor.getString(cursor.getColumnIndex("VisitType")));

                    oVisitCard.setCustomerName(cursor.getString(cursor.getColumnIndex("CustomerName")));
                    oVisitCard.setCustomerID(cursor.getString(cursor.getColumnIndex("CustomerID")));
                    oVisitCard.setVisitTime(cursor.getString(cursor.getColumnIndex("VisitTime")));
                    oVisitCard.setLongitude(cursor.getDouble(cursor.getColumnIndex("Longitude")));
                    oVisitCard.setLatitude(cursor.getDouble(cursor.getColumnIndex("Latitude")));
                    oVisitCard.setLocationAddress(cursor.getString(cursor.getColumnIndex("LocationAddress")));
                    oVisitCard.setVisitNotes(cursor.getString(cursor.getColumnIndex("VisitNotes")));
                    oVisitCard.setSyncDay(cursor.getString(cursor.getColumnIndex("SyncDay")));
                    if(cursor.getString(cursor.getColumnIndex("IsSync")).contains("1")){
                        oVisitCard.setSync(true);
                    }else{
                        oVisitCard.setSync(false);
                    }

                    break;
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return  oVisitCard;
        }catch (Exception ex){Log.d("ERR_LOAD_SM_VISIT",ex.getMessage().toString());}
        return null;
    }

    public String addVisitCard(SM_VisitCard oVisit){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int iSq = 1;

            String VisitCardID=getVisitCardid(oVisit.getVisitDay(),oVisit.getCustomerID());
            if(VisitCardID!=""){
                if(oVisit.getVisitCardID().isEmpty()|| oVisit.getVisitCardID()==null){
                    oVisit.setVisitCardID(VisitCardID);
                }
            }
            iSq=getSizeVisitCard(oVisit.getVisitCardID());
            if (iSq<=0) {
                ContentValues values = new ContentValues();
                values.put("VisitCardID", oVisit.getVisitCardID());
                values.put("VisitDay", oVisit.getVisitDay());
                values.put("VisitType", oVisit.getVisitType());
                values.put("CustomerID", oVisit.getCustomerID());
                values.put("VisitTime", oVisit.getVisitTime());
                values.put("Longitude",oVisit.getLongitude());
                values.put("Latitude", oVisit.getLatitude());
                values.put("LocationAddress", oVisit.getLocationAddress());
                values.put("VisitNotes", oVisit.getVisitNotes());
                values.put("IsSync", oVisit.getSync());

                db.insert("SM_VISITCARD", null, values);
            }else{
                ContentValues values = new ContentValues();
                values.put("VisitDay", oVisit.getVisitDay());
                values.put("CustomerID", oVisit.getCustomerID());
                values.put("VisitTime", oVisit.getVisitTime());
                values.put("Longitude",oVisit.getLongitude());
                values.put("Latitude", oVisit.getLatitude());
                values.put("LocationAddress", oVisit.getLocationAddress());
                values.put("VisitNotes", oVisit.getVisitNotes());
                values.put("IsSync", oVisit.getSync());

                db.update("SM_VISITCARD",values,"VisitCardID=?" ,new String[] {String.valueOf(oVisit.getVisitCardID())});
            }
            //Log.d("INS_VISITCARD_DB","OK");
            db.close();

            return "";
        }catch (Exception e){
            return  "ERR:"+e.getMessage();
        }
    }


    public boolean editVisitCard(SM_VisitCard oVisit){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            //String mSql = String.format("update QR_QRSCAN set PARA_VAL='%s' where PARA_NAME='%s'", ParamName, ParaVal);
            //db.execSQL(mSql);
            ContentValues values = new ContentValues();
            values.put("IsSync", oVisit.getSync());
            values.put("SyncDay",oVisit.getSyncDay());
            db.update("SM_VISITCARD",values,"VisitCardID=?" ,new String[] {String.valueOf(oVisit.getVisitCardID())});
            //Log.d("UDP_SM_VISITCARD_DB","OK");
            db.close();
            return true;
        }catch (Exception e){Log.v("UDP_SM_VISITCARD_ERR",e.getMessage()); return  false;}
    }

    public boolean delVisitCard(SM_VisitCard oVisit){
        try {
            SQLiteDatabase db = getWritableDatabase();
            String mSql=String.format("delete from SM_VISITCARD where VisitCardID='%s'",oVisit.getVisitCardID());
            try {
                db.execSQL(mSql);
                //Log.d("DEL_VISIT_CARD",oVisit.getVisitCardID());
            }catch (Exception ex){
                //Log.d("DEL_VISIT_CARD",ex.getMessage());
                return  false;
            }
            return  true;
        }catch (Exception ex){return false;}
    }


    /********************/
    /*<<SM-ORDER>>*/
    /********************/
    public boolean getCheckOrderStatus(String mToday){
        try {
            SQLiteDatabase db = getReadableDatabase();
            //Cursor cursor = db.rawQuery("SELECT * FROM SM_ORDER WHERE OrderStatus<=3 and (julianday('%s')-julianday(OrderDate)>=7)", new String[]{mToday});
            Cursor cursor = db.rawQuery("SELECT * FROM SM_ORDER WHERE OrderStatus<=3",null);
            int iSq= cursor.getCount();
            cursor.close();

            if (iSq>0){return  true;}else{return  false;}
        }catch(Exception ex){return false;}
    }

    private String getSMOrderID(String OrderDay,String mCustomerID){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM SM_ORDER WHERE (julianday(OrderDate)-julianday('%s')=0) and CustomerID='%s' ", new String[]{OrderDay,mCustomerID});
            String OrderID="";
            if (cursor.moveToFirst()) {
                do {
                    OrderID=cursor.getString(cursor.getColumnIndex("OrderID"));
                    break;
                } while (cursor.moveToNext());
            }
            cursor.close();
            return OrderID;
        }catch(Exception ex){return  "";}
    }

    public int getSizeSMOrder(String OrderID){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM SM_ORDER WHERE OrderID=?", new String[]{OrderID});
            int iSq= cursor.getCount();
            cursor.close();
            return  iSq;
        }catch(Exception ex){return -1;}
    }

    public void CleanSMOrder(int iIntervalDay) {
        try {
            SQLiteDatabase db = getWritableDatabase();

            String mSql = String.format("delete from SM_ORDER_DETAIL where OrderID in(select OrderID from SM_ORDER where julianday('now')-julianday(OrderDate)>%s)", iIntervalDay);
            db.execSQL(mSql);

            mSql = String.format("delete from SM_ORDER where julianday('now')-julianday(OrderDate)>%s", iIntervalDay);
            db.execSQL(mSql);
        } catch (Exception ex) {
        }
    }
    //Fortmat yy
    public List<SM_Order> getAllSMOrder(String fday,String tDay) {
        try {
            List<SM_Order> lstOrder = new ArrayList<SM_Order>();
            String mSql=String.format("Select A.*,B.CustomerName,B.CustomerCode,B.LocationAddress as CustomerAddress from SM_ORDER A LEFT JOIN DM_CUSTOMER B on ifnull(a.CustomerID,'')=b.Customerid "+
                    " where (julianday(A.OrderDate)-julianday('%s')) >=0 and (julianday('%s')-julianday(A.OrderDate)) >=0 order by OrderDate desc,RequestDate desc",fday,tDay);

            //Log.d("SQL_ORDER",mSql);
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            if (cursor.moveToFirst()) {
                do {
                    SM_Order oOrder = new SM_Order();
                    oOrder.setOrderID(cursor.getString(cursor.getColumnIndex("OrderID")));
                    oOrder.setOrderCode(cursor.getString(cursor.getColumnIndex("OrderCode")));
                    oOrder.setCustomerID(cursor.getString(cursor.getColumnIndex("CustomerID")));

                    oOrder.setCustomerName(cursor.getString(cursor.getColumnIndex("CustomerName")));
                    oOrder.setCustomerCode(cursor.getString(cursor.getColumnIndex("CustomerCode")));
                    oOrder.setCustomerAddress(cursor.getString(cursor.getColumnIndex("CustomerAddress")));

                    oOrder.setOrderDate(cursor.getString(cursor.getColumnIndex("OrderDate")));
                    oOrder.setRequestDate(cursor.getString(cursor.getColumnIndex("RequestDate")));
                    oOrder.setMaxDebt(cursor.getInt(cursor.getColumnIndex("MaxDebt")));
                    oOrder.setOriginMoney(cursor.getDouble(cursor.getColumnIndex("OriginMoney")));
                    oOrder.setVAT(cursor.getDouble(cursor.getColumnIndex("VAT")));
                    oOrder.setVATMoney(cursor.getDouble(cursor.getColumnIndex("VATMoney")));
                    oOrder.setTotalMoney(cursor.getDouble(cursor.getColumnIndex("TotalMoney")));
                    oOrder.setOrderStatus(cursor.getInt(cursor.getColumnIndex("OrderStatus")));
                    if(cursor.getString(cursor.getColumnIndex("OrderStatus"))!=null){
                        if(cursor.getString(cursor.getColumnIndex("OrderStatus")).equalsIgnoreCase("0")){
                            oOrder.setOrderStatusDesc("Đơn hàng mới");
                        }else if(cursor.getString(cursor.getColumnIndex("OrderStatus")).equalsIgnoreCase("1")){
                            oOrder.setOrderStatusDesc("Đã điều chỉnh");
                        }else if(cursor.getString(cursor.getColumnIndex("OrderStatus")).equalsIgnoreCase("2")){
                            oOrder.setOrderStatusDesc("Đang chờ xử lý");
                        }else if(cursor.getString(cursor.getColumnIndex("OrderStatus")).equalsIgnoreCase("3")){
                            oOrder.setOrderStatusDesc("Đang vận chuyển");
                        }else if(cursor.getString(cursor.getColumnIndex("OrderStatus")).equalsIgnoreCase("4")){
                            oOrder.setOrderStatusDesc("Đã hoàn tất");
                        }else if(cursor.getString(cursor.getColumnIndex("OrderStatus")).equalsIgnoreCase("5")){
                            oOrder.setOrderStatusDesc("Đã hủy");
                        }else{
                            oOrder.setOrderStatusDesc("");
                        }
                    }
                    oOrder.setApproveDate(cursor.getString(cursor.getColumnIndex("ApproveDate")));
                    oOrder.setHandleStaff(cursor.getString(cursor.getColumnIndex("HandleStaff")));
                    oOrder.setDeliveryDesc(cursor.getString(cursor.getColumnIndex("DeliveryDesc")));

                    oOrder.setLongitude(cursor.getDouble(cursor.getColumnIndex("Longitude")));
                    oOrder.setLatitude(cursor.getDouble(cursor.getColumnIndex("Latitude")));
                    oOrder.setLocationAddress(cursor.getString(cursor.getColumnIndex("LocationAddress")));
                    oOrder.setOrderNotes(cursor.getString(cursor.getColumnIndex("OrderNotes")));
                    oOrder.setPostDay(cursor.getString(cursor.getColumnIndex("PostDay")));
                    if(cursor.getString(cursor.getColumnIndex("IsPost"))!=null && cursor.getString(cursor.getColumnIndex("IsPost")).contains("1")){
                        oOrder.setPost(true);
                    }else{
                        oOrder.setPost(false);
                    }
                    if(cursor.getString(cursor.getColumnIndex("IsSample"))!=null && cursor.getString(cursor.getColumnIndex("IsSample")).contains("1")){
                        oOrder.setSample(true);
                    }else{
                        oOrder.setSample(false);
                    }
                    lstOrder.add(oOrder);

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return lstOrder;
        }catch (Exception ex){Log.d("ERR_LOAD_SM_ORDER",ex.getMessage().toString());}
        return null;
    }

    public SM_Order getSMOrder(String OrderID) {
        try {
            String mSql=String.format("Select A.*,B.CustomerName,B.CustomerCode,B.LocationAddress as CustomerAddress from SM_ORDER A LEFT JOIN DM_CUSTOMER B on a.CustomerID=b.Customerid "+
                    " where A.OrderID='%s' order by OrderDate desc",OrderID);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            SM_Order oOrder = new SM_Order();
            if (cursor.moveToFirst()) {
                do {
                    oOrder.setOrderID(cursor.getString(cursor.getColumnIndex("OrderID")));
                    oOrder.setOrderCode(cursor.getString(cursor.getColumnIndex("OrderCode")));
                    oOrder.setCustomerID(cursor.getString(cursor.getColumnIndex("CustomerID")));

                    oOrder.setCustomerName(cursor.getString(cursor.getColumnIndex("CustomerName")));
                    oOrder.setCustomerCode(cursor.getString(cursor.getColumnIndex("CustomerCode")));
                    oOrder.setCustomerAddress(cursor.getString(cursor.getColumnIndex("CustomerAddress")));

                    oOrder.setOrderDate(cursor.getString(cursor.getColumnIndex("OrderDate")));
                    oOrder.setRequestDate(cursor.getString(cursor.getColumnIndex("RequestDate")));
                    oOrder.setMaxDebt(cursor.getInt(cursor.getColumnIndex("MaxDebt")));
                    oOrder.setOriginMoney(cursor.getDouble(cursor.getColumnIndex("OriginMoney")));
                    oOrder.setVAT(cursor.getDouble(cursor.getColumnIndex("VAT")));
                    oOrder.setVATMoney(cursor.getDouble(cursor.getColumnIndex("VATMoney")));
                    oOrder.setTotalMoney(cursor.getDouble(cursor.getColumnIndex("TotalMoney")));
                    oOrder.setOrderStatus(cursor.getInt(cursor.getColumnIndex("OrderStatus")));
                    if(cursor.getString(cursor.getColumnIndex("OrderStatus"))!=null){
                        if(cursor.getString(cursor.getColumnIndex("OrderStatus")).equalsIgnoreCase("0")){
                            oOrder.setOrderStatusDesc("Đơn hàng mới");
                        }else if(cursor.getString(cursor.getColumnIndex("OrderStatus")).equalsIgnoreCase("1")){
                            oOrder.setOrderStatusDesc("Đã điều chỉnh");
                        }else if(cursor.getString(cursor.getColumnIndex("OrderStatus")).equalsIgnoreCase("2")){
                            oOrder.setOrderStatusDesc("Đang chờ xử lý");
                        }else if(cursor.getString(cursor.getColumnIndex("OrderStatus")).equalsIgnoreCase("3")){
                            oOrder.setOrderStatusDesc("Đang vận chuyển");
                        }else if(cursor.getString(cursor.getColumnIndex("OrderStatus")).equalsIgnoreCase("4")){
                            oOrder.setOrderStatusDesc("Đã hoàn tất");
                        }else if(cursor.getString(cursor.getColumnIndex("OrderStatus")).equalsIgnoreCase("5")){
                            oOrder.setOrderStatusDesc("Đã hủy");
                        }else{
                            oOrder.setOrderStatusDesc("");
                        }
                    }
                    oOrder.setApproveDate(cursor.getString(cursor.getColumnIndex("ApproveDate")));
                    oOrder.setHandleStaff(cursor.getString(cursor.getColumnIndex("HandleStaff")));
                    oOrder.setDeliveryDesc(cursor.getString(cursor.getColumnIndex("DeliveryDesc")));

                    oOrder.setLongitude(cursor.getDouble(cursor.getColumnIndex("Longitude")));
                    oOrder.setLatitude(cursor.getDouble(cursor.getColumnIndex("Latitude")));
                    oOrder.setLocationAddress(cursor.getString(cursor.getColumnIndex("LocationAddress")));
                    oOrder.setOrderNotes(cursor.getString(cursor.getColumnIndex("OrderNotes")));

                    oOrder.setPostDay(cursor.getString(cursor.getColumnIndex("PostDay")));
                    if(cursor.getString(cursor.getColumnIndex("IsPost")).contains("1")){
                        oOrder.setPost(true);
                    }else{
                        oOrder.setPost(false);
                    }

                    if(cursor.getString(cursor.getColumnIndex("IsSample")).contains("1")){
                        oOrder.setSample(true);
                    }else{
                        oOrder.setSample(false);
                    }
                    break;
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return  oOrder;
        }catch (Exception ex){Log.d("ERR_LOAD_SM_ORDER",ex.getMessage().toString());}
        return null;
    }

    public String addSMOrder(SM_Order oOrder){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int iSq = 1;

            String OrderID=getSMOrderID(oOrder.getOrderDate(),oOrder.getCustomerID());
            if(OrderID!=""){
                if(oOrder.getOrderID().isEmpty()|| oOrder.getOrderID()==null){
                    oOrder.setOrderID(OrderID);
                }
            }
            iSq=getSizeSMOrder(oOrder.getOrderID());
            if (iSq<=0) {
                ContentValues values = new ContentValues();
                values.put("OrderID", oOrder.getOrderID());
                values.put("OrderCode", oOrder.getOrderCode());
                values.put("OrderDate", oOrder.getOrderDate());
                values.put("RequestDate", oOrder.getRequestDate());
                values.put("MaxDebt", oOrder.getMaxDebt());
                values.put("OriginMoney",oOrder.getOriginMoney());
                values.put("VAT", oOrder.getVAT());
                values.put("VATMoney", oOrder.getVATMoney());
                values.put("TotalMoney", oOrder.getTotalMoney());
                values.put("OrderStatus", oOrder.getOrderStatus());
                values.put("OrderNotes", oOrder.getOrderNotes());
                values.put("CustomerID", oOrder.getCustomerID());

                values.put("Longitude",oOrder.getLongitude());
                values.put("Latitude", oOrder.getLatitude());
                values.put("LocationAddress", oOrder.getLocationAddress());
                values.put("SeqnoCode", oOrder.getSeqnoCode());
                values.put("IsPost", oOrder.getPost());
                values.put("PostDay", oOrder.getPostDay());
                values.put("IsSample", oOrder.getSample());

                db.insert("SM_ORDER", null, values);
            }else{
                ContentValues values = new ContentValues();
                values.put("OrderCode", oOrder.getOrderCode());
                values.put("OrderDate", oOrder.getOrderDate());
                values.put("RequestDate", oOrder.getRequestDate());
                values.put("MaxDebt", oOrder.getMaxDebt());
                values.put("OriginMoney",oOrder.getOriginMoney());
                values.put("VAT", oOrder.getVAT());
                values.put("VATMoney", oOrder.getVATMoney());
                values.put("TotalMoney", oOrder.getTotalMoney());
                values.put("OrderStatus", oOrder.getOrderStatus());
                values.put("OrderNotes", oOrder.getOrderNotes());
                values.put("CustomerID", oOrder.getCustomerID());

                values.put("Longitude",oOrder.getLongitude());
                values.put("Latitude", oOrder.getLatitude());
                values.put("LocationAddress", oOrder.getLocationAddress());
                values.put("IsPost", oOrder.getPost());
                values.put("PostDay", oOrder.getPostDay());
                values.put("IsSample", oOrder.getSample());
                db.update("SM_ORDER",values,"OrderID=?" ,new String[] {String.valueOf(oOrder.getOrderID())});
            }
            //Log.d("INS_SM_ORDER_DB","OK");
            db.close();
            return "";
        }catch (Exception e){
            return  "ERR:"+e.getMessage();
        }
    }

    public boolean editSMOrder(SM_Order oOrder){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("OrderCode", oOrder.getOrderCode());
            values.put("OrderDate", oOrder.getOrderDate());
            values.put("RequestDate", oOrder.getRequestDate());
            values.put("MaxDebt", oOrder.getMaxDebt());
            values.put("OriginMoney",oOrder.getOriginMoney());
            values.put("VAT", oOrder.getVAT());
            values.put("VATMoney", oOrder.getVATMoney());
            values.put("TotalMoney", oOrder.getTotalMoney());
            values.put("OrderStatus", oOrder.getOrderStatus());
            values.put("CustomerID", oOrder.getCustomerID());
            values.put("Longitude",oOrder.getLongitude());
            values.put("Latitude", oOrder.getLatitude());
            values.put("LocationAddress", oOrder.getLocationAddress());
            values.put("SeqnoCode", oOrder.getSeqnoCode());
            values.put("IsPost", oOrder.getPost());
            values.put("PostDay", oOrder.getPostDay());
            values.put("IsSample", oOrder.getSample());
            db.update("SM_ORDER",values,"OrderID=?" ,new String[] {String.valueOf(oOrder.getOrderID())});
            db.close();
            return true;
        }catch (Exception e){Log.v("UDP_SM_ORDER_ERR",e.getMessage()); return  false;}
    }

    public boolean editSMOrderStatus(SM_Order oOrder){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("OrderStatus", oOrder.getOrderStatus());
            values.put("IsPost", oOrder.getPost());
            values.put("PostDay", oOrder.getPostDay());
            db.update("SM_ORDER",values,"OrderID=?" ,new String[] {String.valueOf(oOrder.getOrderID())});
            db.close();
            return true;
        }catch (Exception e){Log.v("UDP_SM_ORDER_ERR",e.getMessage()); return  false;}
    }


    public boolean editSMOrderStatus2(SM_OrderStatus oOST){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("OrderStatus",oOST.getOrderStatus());
            values.put("TotalMoney", oOST.getTotalMoney());

            db.update("SM_ORDER",values,"OrderCode=?" ,new String[] {String.valueOf(oOST.getOrderCode())});
            db.close();
            return true;
        }catch (Exception e){Log.v("UDP_SM_ORDER_ERR",e.getMessage()); return  false;}
    }

    public boolean delSMOrder(SM_Order oOrder){
        try {
            SQLiteDatabase db = getWritableDatabase();
            String mSql=String.format("delete from SM_ORDER_DETAIL where OrderID IN(select OrderID from SM_ORDER where OrderID='%s')",oOrder.getOrderID());
            try {
                db.execSQL(mSql);
                mSql=String.format("delete from SM_ORDER where OrderID='%s'",oOrder.getOrderID());
                db.execSQL(mSql);
            }catch (Exception ex){
                return  false;
            }
            return  true;
        }catch (Exception ex){return false;}
    }



    /*******************/
    /*SM_ORDER_DETAIL*/
    /*******************/
    private String getSMOrderDetailID(String mOrderID,String mProductID){
        try {
            SQLiteDatabase db = getReadableDatabase();
            String mSql=String.format("SELECT * FROM SM_ORDER_DETAIL WHERE OrderID='%s' AND ProductID='%s'",mOrderID,mProductID);
            Cursor cursor = db.rawQuery(mSql,null);
            String OrderDetailID="";
            if (cursor.moveToFirst()) {
                do {
                    OrderDetailID=cursor.getString(cursor.getColumnIndex("OrderDetailID"));
                    break;
                } while (cursor.moveToNext());
            }
            cursor.close();
            return OrderDetailID;
        }catch(Exception ex){return  "";}
    }

    public int getSizeSMOrderDetail(String OrderDetailID){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM SM_ORDER_DETAIL WHERE OrderDetailID=?", new String[]{OrderDetailID});
            int iSq= cursor.getCount();
            cursor.close();
            return  iSq;
        }catch(Exception ex){return -1;}
    }


    public List<SM_OrderDetail> getAllSMOrderDetail(String mOrderID) {
        try {
            List<SM_OrderDetail> lstOrderDetail = new ArrayList<SM_OrderDetail>();
            String mSql=String.format("Select * from SM_ORDER_DETAIL where OrderID='%s' order by OrderDetailID desc", mOrderID);

            //Log.d("SQL_ORDER_DETAIL",mSql);
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            if (cursor.moveToFirst()) {
                do {
                    SM_OrderDetail oOrderDetail = new SM_OrderDetail();
                    oOrderDetail.setOrderDetailID(cursor.getString(cursor.getColumnIndex("OrderDetailID")));
                    oOrderDetail.setOrderID(cursor.getString(cursor.getColumnIndex("OrderID")));

                    oOrderDetail.setProductID(cursor.getString(cursor.getColumnIndex("ProductID")));
                    oOrderDetail.setProductCode(cursor.getString(cursor.getColumnIndex("ProductCode")));
                    oOrderDetail.setProductName(cursor.getString(cursor.getColumnIndex("ProductName")));
                    oOrderDetail.setUnit(cursor.getString(cursor.getColumnIndex("Unit")));
                    oOrderDetail.setSpec(cursor.getString(cursor.getColumnIndex("Spec")));
                    oOrderDetail.setConvertBox(cursor.getFloat(cursor.getColumnIndex("ConvertBox")));
                    oOrderDetail.setAmount(cursor.getInt(cursor.getColumnIndex("Amount")));
                    oOrderDetail.setAmountBox(cursor.getFloat(cursor.getColumnIndex("AmountBox")));
                    oOrderDetail.setPrice(cursor.getDouble(cursor.getColumnIndex("Price")));
                    oOrderDetail.setOriginMoney(cursor.getDouble(cursor.getColumnIndex("OriginMoney")));
                    oOrderDetail.setNotes(cursor.getString(cursor.getColumnIndex("Notes")));
                    oOrderDetail.setNotes2(cursor.getString(cursor.getColumnIndex("Notes2")));

                    lstOrderDetail.add(oOrderDetail);

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return lstOrderDetail;
        }catch (Exception ex){Log.d("ERR_LOAD_ORDER_DETAIL",ex.getMessage().toString());}
        return null;
    }

    public SM_OrderDetail getSMOrderDetail(String mOrderDetailID) {
        try {
            String mSql=String.format("Select * from SM_ORDER_DETAIL where OrderDetailID='%$' order by OrderDetailID desc",mOrderDetailID);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);

            SM_OrderDetail oOrderDetail = new SM_OrderDetail();
            if (cursor.moveToFirst()) {
                do {
                    oOrderDetail.setOrderDetailID(cursor.getString(cursor.getColumnIndex("OrderDetailID")));
                    oOrderDetail.setOrderID(cursor.getString(cursor.getColumnIndex("OrderID")));

                    oOrderDetail.setProductID(cursor.getString(cursor.getColumnIndex("ProductID")));
                    oOrderDetail.setProductCode(cursor.getString(cursor.getColumnIndex("ProductCode")));
                    oOrderDetail.setProductName(cursor.getString(cursor.getColumnIndex("ProductName")));
                    oOrderDetail.setUnit(cursor.getString(cursor.getColumnIndex("Unit")));
                    oOrderDetail.setSpec(cursor.getString(cursor.getColumnIndex("Spec")));
                    oOrderDetail.setConvertBox(cursor.getFloat(cursor.getColumnIndex("ConvertBox")));
                    oOrderDetail.setAmount(cursor.getInt(cursor.getColumnIndex("Amount")));
                    oOrderDetail.setAmountBox(cursor.getFloat(cursor.getColumnIndex("AmountBox")));
                    oOrderDetail.setPrice(cursor.getDouble(cursor.getColumnIndex("Price")));
                    oOrderDetail.setOriginMoney(cursor.getDouble(cursor.getColumnIndex("OriginMoney")));
                    oOrderDetail.setNotes(cursor.getString(cursor.getColumnIndex("Notes")));
                    oOrderDetail.setNotes2(cursor.getString(cursor.getColumnIndex("Notes2")));
                    break;
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return oOrderDetail;
        }catch (Exception ex){Log.d("ERR_LOAD_ORDER_DETAIL",ex.getMessage().toString());}
        return null;
    }


    public String addSMOrderDetail(SM_OrderDetail oOrderDetail){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int iSq = 1;

            String mOrderDetailID=getSMOrderDetailID(oOrderDetail.getOrderID(),oOrderDetail.getProductID());
            if(mOrderDetailID.isEmpty()|| mOrderDetailID=="" ){
                ContentValues values = new ContentValues();
                values.put("OrderDetailID", oOrderDetail.getOrderDetailID());
                values.put("OrderID", oOrderDetail.getOrderID());
                values.put("ProductID", oOrderDetail.getProductID());
                values.put("ProductCode", oOrderDetail.getProductCode());
                values.put("ProductName", oOrderDetail.getProductName());
                values.put("Unit", oOrderDetail.getUnit());
                values.put("Spec",oOrderDetail.getSpec());
                values.put("ConvertBox",oOrderDetail.getConvertBox());
                values.put("Amount", oOrderDetail.getAmount());
                values.put("AmountBox", oOrderDetail.getAmountBox());
                values.put("Price",oOrderDetail.getPrice());
                values.put("OriginMoney", oOrderDetail.getOriginMoney());
                values.put("Notes", oOrderDetail.getNotes());
                values.put("Notes2", oOrderDetail.getNotes2());

                db.insert("SM_ORDER_DETAIL", null, values);
            }else{
                ContentValues values = new ContentValues();
                values.put("OrderDetailID", oOrderDetail.getOrderDetailID());
                values.put("OrderID", oOrderDetail.getOrderID());
                values.put("ProductID", oOrderDetail.getProductID());
                values.put("ProductCode", oOrderDetail.getProductCode());
                values.put("ProductName", oOrderDetail.getProductName());
                values.put("Unit", oOrderDetail.getUnit());
                values.put("Spec",oOrderDetail.getSpec());
                values.put("ConvertBox",oOrderDetail.getConvertBox());
                values.put("Amount", oOrderDetail.getAmount());
                values.put("AmountBox", oOrderDetail.getAmountBox());
                values.put("Price",oOrderDetail.getPrice());
                values.put("OriginMoney", oOrderDetail.getOriginMoney());
                values.put("Notes", oOrderDetail.getNotes());
                values.put("Notes2", oOrderDetail.getNotes2());

                db.update("SM_ORDER_DETAIL",values,"OrderDetailID=?" ,new String[] {String.valueOf(oOrderDetail.getOrderDetailID())});
            }

            //Log.d("INS_ORDERDETAIL_DB","OK");
            db.close();
            return "";
        }catch (Exception e){
            return  "ERR:"+e.getMessage();
        }
    }

    public boolean delSMOrderDetail(SM_OrderDetail oOrderDetail){
        try {
            SQLiteDatabase db = getWritableDatabase();
            String mSql=String.format("delete from SM_ORDER_DETAIL where OrderDetailID ='%s'",oOrderDetail.getOrderDetailID());
            try {
                db.execSQL(mSql);
            }catch (Exception ex){
                //Log.d("DEL_SM_ORDER",ex.getMessage());
                return  false;
            }
            return  true;
        }catch (Exception ex){return false;}
    }


    public boolean delAllSMOrderDetail(String mOrderID){
        try {
            SQLiteDatabase db = getWritableDatabase();
            String mSql=String.format("delete from SM_ORDER_DETAIL where OrderID ='%s'",mOrderID);
            try {
                db.execSQL(mSql);
            }catch (Exception ex){
                Log.d("DEL_SM_ODT",ex.getMessage());
                return  false;
            }
            return  true;
        }catch (Exception ex){return false;}
    }


    /****SM_DELIVERY_ORDER****/
    private String getDeliveryID(String mOrderID,String mTransportCode){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM SM_DELIVERY_ORDER WHERE (OrderID='%s' AND TransportCode='%s')", new String[]{mOrderID,mTransportCode});
            String mDeliveryOrderID="";
            if (cursor.moveToFirst()) {
                do {
                    mDeliveryOrderID=cursor.getString(cursor.getColumnIndex("DeliveryOrderID"));
                    break;
                } while (cursor.moveToNext());
            }
            cursor.close();
            return mDeliveryOrderID;
        }catch(Exception ex){return  "";}
    }

    public List<SM_OrderDelivery> getAllDelivery(String mOrderID) {
        try {
            List<SM_OrderDelivery> lstOrderDetail = new ArrayList<SM_OrderDelivery>();
            String mSql=String.format("Select A.* from SM_DELIVERY_ORDER A  where A.OrderID='%s' order by DeliveryNum asc",mOrderID);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            if (cursor.moveToFirst()) {
                do {
                    SM_OrderDelivery oOrderDelivery = new SM_OrderDelivery();
                    oOrderDelivery.setDeliveryOrderID(cursor.getString(cursor.getColumnIndex(    "DeliveryOrderID")));
                    oOrderDelivery.setOrderID(cursor.getString(cursor.getColumnIndex("OrderID")));
                    oOrderDelivery.setTransportCode(cursor.getString(cursor.getColumnIndex("TransportCode")));
                    oOrderDelivery.setNumberPlate(cursor.getString(cursor.getColumnIndex("NumberPlate")));
                    oOrderDelivery.setCarType(cursor.getString(cursor.getColumnIndex("CarType")));
                    oOrderDelivery.setDeliveryStaff(cursor.getString(cursor.getColumnIndex("DeliveryStaff")));
                    oOrderDelivery.setDeliveryNum(cursor.getInt(cursor.getColumnIndex("DeliveryNum")));
                    oOrderDelivery.setDeliveryDate(cursor.getString(cursor.getColumnIndex("DeliveryDate")));
                    oOrderDelivery.setHandlingStaff(cursor.getString(cursor.getColumnIndex("HandlingStaff")));
                    oOrderDelivery.setHandlingDate(cursor.getString(cursor.getColumnIndex("HandlingDate")));
                    oOrderDelivery.setTotalMoney(cursor.getFloat(cursor.getColumnIndex("TotalMoney")));
                    oOrderDelivery.setDeliveryDesc(cursor.getString(cursor.getColumnIndex("DeliveryDesc")));
                    lstOrderDetail.add(oOrderDelivery);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return lstOrderDetail;
        }catch (Exception ex){Log.d("ERR_LOAD_ORDER_DETAIL",ex.getMessage().toString());}
        return null;
    }

    public String addDelivery(SM_OrderDelivery oOrderDelivery){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int iSq = 1;
            String mOrderDeliveryID=getDeliveryID(oOrderDelivery.getOrderID(),oOrderDelivery.getTransportCode());
            if(mOrderDeliveryID==null || mOrderDeliveryID.isEmpty()|| mOrderDeliveryID=="" ){
                ContentValues values = new ContentValues();
                values.put("DeliveryOrderID", oOrderDelivery.getDeliveryOrderID());
                values.put("OrderID", oOrderDelivery.getOrderID());
                values.put("TransportCode",oOrderDelivery.getTransportCode());
                values.put("NumberPlate", oOrderDelivery.getNumberPlate());
                values.put("CarType", oOrderDelivery.getCarType());
                values.put("DeliveryStaff", oOrderDelivery.getDeliveryStaff());
                values.put("DeliveryNum",oOrderDelivery.getDeliveryNum());
                values.put("DeliveryDate",oOrderDelivery.getDeliveryDate());
                values.put("HandlingStaff", oOrderDelivery.getHandlingStaff());
                values.put("HandlingDate",oOrderDelivery.getHandlingDate());
                values.put("TotalMoney",oOrderDelivery.getTotalMoney());
                values.put("DeliveryDesc",oOrderDelivery.getDeliveryDesc());

                db.insert("SM_DELIVERY_ORDER", null, values);
            }else{
                ContentValues values = new ContentValues();
                values.put("DeliveryOrderID", oOrderDelivery.getDeliveryOrderID());
                values.put("OrderID", oOrderDelivery.getOrderID());
                values.put("TransportCode",oOrderDelivery.getTransportCode());
                values.put("NumberPlate", oOrderDelivery.getNumberPlate());
                values.put("CarType", oOrderDelivery.getCarType());
                values.put("DeliveryStaff", oOrderDelivery.getDeliveryStaff());
                values.put("DeliveryNum",oOrderDelivery.getDeliveryNum());
                values.put("DeliveryDate",oOrderDelivery.getDeliveryDate());
                values.put("HandlingStaff", oOrderDelivery.getHandlingStaff());
                values.put("HandlingDate",oOrderDelivery.getHandlingDate());
                values.put("TotalMoney",oOrderDelivery.getTotalMoney());
                values.put("DeliveryDesc",oOrderDelivery.getDeliveryDesc());
                db.update("SM_DELIVERY_ORDER",values,"DeliveryOrderID=?" ,new String[] {String.valueOf(mOrderDeliveryID)});
            }
            db.close();
            return "";
        }catch (Exception e){
            return  "ERR:"+e.getMessage();
        }
    }


    public boolean delDelivery(String mDeliveryID){
        try {
            SQLiteDatabase db = getWritableDatabase();
            String mSql=String.format("delete from SM_DELIVERY_ORDER_DETAIL where DeliveryOrderID ='%s'",mDeliveryID);
            try {
                db.execSQL(mSql);

                mSql=String.format("delete from SM_DELIVERY_ORDER where DeliveryOrderID ='%s'",mDeliveryID);
                db.execSQL(mSql);
                return  true;
            }catch (Exception ex){
                return  false;
            }
        }catch (Exception ex){return false;}
    }

    //DELIVERY DETAIL
    public List<SM_OrderDeliveryDetail> getAllDeliveryDetail(String mDeliveryOrderID) {
        try {
            List<SM_OrderDeliveryDetail> lstOrderDetail = new ArrayList<SM_OrderDeliveryDetail>();
            String mSql=String.format("Select A.* from SM_DELIVERY_ORDER_DETAIL A  where A.DeliveryOrderID='%s' order by ProductCode asc",mDeliveryOrderID);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            if (cursor.moveToFirst()) {
                do {
                    SM_OrderDeliveryDetail oDLT = new SM_OrderDeliveryDetail();
                    oDLT.setDeliveryOrderDetailID(cursor.getString(cursor.getColumnIndex(    "DeliveryOrderDetailID")));
                    oDLT.setDeliveryOrderID(cursor.getString(cursor.getColumnIndex(    "DeliveryOrderID")));

                    oDLT.setProductCode(cursor.getString(cursor.getColumnIndex(    "ProductCode")));
                    oDLT.setProductName(cursor.getString(cursor.getColumnIndex(    "ProductName")));
                    oDLT.setUnit(cursor.getString(cursor.getColumnIndex(    "Unit")));
                    oDLT.setSpec(cursor.getString(cursor.getColumnIndex(    "Spec")));
                    oDLT.setAmount(cursor.getInt(cursor.getColumnIndex(    "Amount")));
                    oDLT.setAmountBox(cursor.getFloat(cursor.getColumnIndex(    "AmountBox")));
                    oDLT.setPrice(cursor.getFloat(cursor.getColumnIndex(    "Price")));
                    oDLT.setOriginMoney(cursor.getFloat(cursor.getColumnIndex(    "OriginMoney")));
                    oDLT.setNotes(cursor.getString(cursor.getColumnIndex(    "Notes")));
                    lstOrderDetail.add(oDLT);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return lstOrderDetail;
        }catch (Exception ex){Log.d("ERR_LOAD_ORDER_DETAIL",ex.getMessage().toString());}
        return null;
    }

    private String getDeliveryDetailID(String mDeliveryOrderID,String mProductCode){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM SM_DELIVERY_ORDER_DETAIL WHERE (DeliveryOrderID='%s' AND ProductCode='%s')", new String[]{mDeliveryOrderID,mProductCode});
            String mDeliveryOrderDetailID="";
            if (cursor.moveToFirst()) {
                do {
                    mDeliveryOrderDetailID=cursor.getString(cursor.getColumnIndex("DeliveryOrderDetailID"));
                    break;
                } while (cursor.moveToNext());
            }
            cursor.close();
            return  mDeliveryOrderDetailID;
        }catch(Exception ex){return  "";}
    }


    public String addDeliveryDetail(SM_OrderDeliveryDetail oDLT){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int iSq = 1;
            String mDeliveryDetailID=getDeliveryDetailID(oDLT.getDeliveryOrderID(),oDLT.getProductCode());
            if(mDeliveryDetailID==null || mDeliveryDetailID.isEmpty()|| mDeliveryDetailID=="" ){
                ContentValues values = new ContentValues();
                values.put("DeliveryOrderDetailID",oDLT.getDeliveryOrderDetailID());
                values.put("DeliveryOrderID", oDLT.getDeliveryOrderID());
                values.put("ProductCode",oDLT.getProductCode());
                values.put("ProductName", oDLT.getProductName());
                values.put("Unit", oDLT.getUnit());
                values.put("Spec", oDLT.getSpec());
                values.put("Amount",oDLT.getAmount());
                values.put("AmountBox",oDLT.getAmountBox());
                values.put("Price",oDLT.getPrice());
                values.put("OriginMoney", oDLT.getOriginMoney());
                values.put("Notes",oDLT.getNotes());

                db.insert("SM_DELIVERY_ORDER_DETAIL", null, values);
            }else{
                ContentValues values = new ContentValues();
                values.put("DeliveryOrderID", oDLT.getDeliveryOrderID());
                values.put("ProductCode",oDLT.getProductCode());
                values.put("ProductName", oDLT.getProductName());
                values.put("Unit", oDLT.getUnit());
                values.put("Spec", oDLT.getSpec());
                values.put("Amount",oDLT.getAmount());
                values.put("AmountBox",oDLT.getAmountBox());
                values.put("Price",oDLT.getPrice());
                values.put("OriginMoney", oDLT.getOriginMoney());
                values.put("Notes",oDLT.getNotes());
                db.update("SM_DELIVERY_ORDER_DETAIL",values,"DeliveryOrderDetailID=?" ,new String[] {String.valueOf(mDeliveryDetailID)});
            }
            db.close();
            return "";
        }catch (Exception e){
            return  "ERR:"+e.getMessage();
        }
    }

    public boolean delAllDeliveryDetail(String mDeliveryID){
        try {
            SQLiteDatabase db = getWritableDatabase();
            String mSql=String.format("delete from SM_DELIVERY_ORDER_DETAIL where DeliveryOrderID ='%s'",mDeliveryID);
            try {
                db.execSQL(mSql);
                return  true;
            }catch (Exception ex){
                return  false;
            }
        }catch (Exception ex){return false;}
    }





    /*******GĐ-2*************/
    /****DM_TREE*****/
    public int getSizeTree(String Treeid){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM DM_TREE WHERE TreeID=?",new String[]{Treeid});
            int iSq= cursor.getCount();
            cursor.close();
            return  iSq;
        }catch(Exception ex){return 0;}
    }

    public boolean addTree(DM_Tree oDM){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int iSq = 1;

            iSq=getSizeTree(Integer.toString(oDM.getTreeID()));
            if (iSq<=0) {
                ContentValues values = new ContentValues();
                values.put("TreeID",oDM.getTreeID());
                values.put("TreeCode",oDM.getTreeCode());
                values.put("TreeGroupCode", oDM.getTreeGroupCode());
                values.put("TreeName", oDM.getTreeName());
                db.insert("DM_TREE", null, values);
            }else{
                iSq=iSq+1;
                ContentValues values = new ContentValues();
                values.put("TreeCode",oDM.getTreeCode());
                values.put("TreeGroupCode", oDM.getTreeGroupCode());
                values.put("TreeName", oDM.getTreeName());
                db.update("DM_TREE",values,"TreeID=?" ,new String[] { String.valueOf(oDM.getTreeID())});
            }
            db.close();

            return true;
        }catch (Exception e){Log.v("INS_DISTRICT_ERR",e.getMessage()); return  false;}
    }

    public List<DM_Tree> getAllTree() {
        List<DM_Tree> lstPara = new ArrayList<DM_Tree>();

        String selectQuery = "SELECT  TreeID,TreeCode,TreeGroupCode,TreeName FROM DM_TREE ORDER BY TreeName ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                DM_Tree oPara = new DM_Tree();
                oPara.setTreeID(cursor.getInt(cursor.getColumnIndex("TreeID")));
                oPara.setTreeCode(cursor.getString(cursor.getColumnIndex("TreeCode")));
                oPara.setTreeGroupCode(cursor.getString(cursor.getColumnIndex("TreeGroupCode")));
                oPara.setTreeName(cursor.getString(cursor.getColumnIndex("TreeName")));

                lstPara.add(oPara);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();


        return lstPara;
    }
    public List<DM_Tree> getTree(Integer TreeID) {
        List<DM_Tree> lstPara = new ArrayList<DM_Tree>();
        String selectQuery = String.format("SELECT  TreeID,TreeCode,TreeGroupCode,TreeName FROM DM_TREE where TreeID='%d' ORDER BY TreeName ASC",TreeID);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                DM_Tree oPara = new DM_Tree();
                oPara.setTreeID(cursor.getInt(cursor.getColumnIndex("TreeID")));
                oPara.setTreeCode(cursor.getString(cursor.getColumnIndex("TreeCode")));
                oPara.setTreeGroupCode(cursor.getString(cursor.getColumnIndex("TreeGroupCode")));
                oPara.setTreeName(cursor.getString(cursor.getColumnIndex("TreeName")));
                lstPara.add(oPara);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lstPara;
    }

    public boolean delTree(String TreeID,boolean isAll){
        try {
            String mSql;
            SQLiteDatabase db = getWritableDatabase();
            if(isAll) {
                mSql = String.format("delete from DM_TREE");
            }else{
                mSql = String.format("delete from DM_TREE where TreeID='%s'",TreeID);
            }
            try {
                db.execSQL(mSql);
            }catch (Exception ex){
                return  false;
            }
            return  true;
        }catch (Exception ex){return false;}
    }



    /****DM_TREE_DISEASE [DỊCH HẠI]*****/
    public int getSizeTreeDisease(String DiseaseID){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM DM_TREE_DISEASE WHERE DiseaseID=?",new String[]{DiseaseID});
            int iSq= cursor.getCount();
            cursor.close();
            return  iSq;
        }catch(Exception ex){return 0;}
    }

    public boolean addTreeDisease(DM_Tree_Disease oDM){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int iSq = 1;

            iSq=getSizeTreeDisease(Integer.toString(oDM.getDiseaseID()));
            if (iSq<=0) {
                ContentValues values = new ContentValues();
                values.put("DiseaseID",oDM.getDiseaseID());
                values.put("TreeID",oDM.getTreeID());
                values.put("DiseaseCode",oDM.getDiseaseCode());
                values.put("DiseaseName",oDM.getDiseaseName());
                db.insert("DM_TREE_DISEASE", null, values);
            }else{
                iSq=iSq+1;
                ContentValues values = new ContentValues();
                values.put("TreeID",oDM.getTreeID());
                values.put("DiseaseCode",oDM.getDiseaseCode());
                values.put("DiseaseName", oDM.getDiseaseName());
                db.update("DM_TREE_DISEASE",values,"DiseaseID=?" ,new String[] { String.valueOf(oDM.getDiseaseID())});
            }
            db.close();
            return true;
        }catch (Exception e){Log.v("INS_DISEASE_ERR",e.getMessage()); return  false;}
    }

    public List<DM_Tree_Disease> getAllTreeDisease() {
        List<DM_Tree_Disease> lstPara = new ArrayList<DM_Tree_Disease>();

        String selectQuery = "SELECT  A.DiseaseID,A.TreeID,A.DiseaseCode,A.DiseaseName,B.TreeName FROM DM_TREE_DISEASE A LEFT JOIN DM_TREE B ON A.TreeID=B.TreeID ORDER BY A.TreeID, DiseaseName ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                DM_Tree_Disease oPara = new DM_Tree_Disease();
                oPara.setDiseaseID(cursor.getInt(cursor.getColumnIndex("DiseaseID")));
                oPara.setTreeID(cursor.getInt(cursor.getColumnIndex("TreeID")));
                oPara.setDiseaseCode(cursor.getString(cursor.getColumnIndex("DiseaseCode")));
                oPara.setDiseaseName(cursor.getString(cursor.getColumnIndex("DiseaseName")));
                oPara.setTreeName(cursor.getString(cursor.getColumnIndex("TreeName")));
                lstPara.add(oPara);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();


        return lstPara;
    }
    public List<DM_Tree_Disease> getTreeDisease(Integer DiseaseID) {
        List<DM_Tree_Disease> lstPara = new ArrayList<DM_Tree_Disease>();
        String selectQuery = String.format("SELECT  A.DiseaseID,A.TreeID,A.DiseaseCode,A.DiseaseName,B.TreeName FROM DM_TREE_DISEASE A LEFT JOIN DM_TREE B ON A.TreeID=B.TreeID where A.DiseaseID='%d' ORDER BY A.TreeID,DiseaseName ASC",DiseaseID);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                DM_Tree_Disease oPara = new DM_Tree_Disease();
                oPara.setDiseaseID(cursor.getInt(cursor.getColumnIndex("DiseaseID")));
                oPara.setTreeID(cursor.getInt(cursor.getColumnIndex("TreeID")));
                oPara.setDiseaseCode(cursor.getString(cursor.getColumnIndex("DiseaseCode")));
                oPara.setDiseaseName(cursor.getString(cursor.getColumnIndex("DiseaseName")));
                oPara.setTreeName(cursor.getString(cursor.getColumnIndex("TreeName")));
                lstPara.add(oPara);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lstPara;
    }

    public boolean delTreeDisease(String DiseaseID,boolean isAll){
        try {
            String mSql;
            SQLiteDatabase db = getWritableDatabase();
            if(isAll) {
                mSql = String.format("delete from DM_TREE_DISEASE");
            }else{
                mSql = String.format("delete from DM_TREE_DISEASE where DiseaseID='%s'",DiseaseID);
            }
            try {
                db.execSQL(mSql);
            }catch (Exception ex){
                return  false;
            }
            return  true;
        }catch (Exception ex){return false;}
    }



    /*<<SM-CUSTOMER-PAY>>*/
    /********************/
    public boolean getCheckPayStatus(String mToday){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM SM_CUSTOMER_PAY WHERE PayStatus<=3",null);
            int iSq= cursor.getCount();
            cursor.close();
            if (iSq>0){return  true;}else{return  false;}
        }catch(Exception ex){return false;}
    }
    private String getPayID(String PayDay,String mCustomerID){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM SM_CUSTOMER_PAY WHERE (julianday(OrderDate)-julianday('%s')=0) and CustomerID='%s' ", new String[]{PayDay,mCustomerID});
            String PayID="";
            if (cursor.moveToFirst()) {
                do {
                    PayID=cursor.getString(cursor.getColumnIndex("PayID"));
                    break;
                } while (cursor.moveToNext());
            }
            cursor.close();
            return PayID;
        }catch(Exception ex){return  "";}
    }
    public int getSizePay(String PayID){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM SM_CUSTOMER_PAY WHERE PayID=?", new String[]{PayID});
            int iSq= cursor.getCount();
            cursor.close();
            return  iSq;
        }catch(Exception ex){return -1;}
    }

    public void CleanCustomerPay(int iIntervalDay) {
        try {
            SQLiteDatabase db = getWritableDatabase();

            String mSql = String.format("delete from SM_CUSTOMER_PAY where PayID in(select PayID from SM_CUSTOMER_PAY where julianday('now')-julianday(PayDate)>%s)", iIntervalDay);
            db.execSQL(mSql);

            mSql = String.format("delete from SM_CUSTOMER_PAY where julianday('now')-julianday(PayDate)>%s", iIntervalDay);
            db.execSQL(mSql);
        } catch (Exception ex) {
        }
    }
    public List<SM_CustomerPay> getAllCustomerPay(String fday, String tDay) {
        try {
            List<SM_CustomerPay> lstPay = new ArrayList<SM_CustomerPay>();
            String mSql=String.format("Select A.*,B.CustomerName,B.CustomerCode from SM_CUSTOMER_PAY A LEFT JOIN DM_CUSTOMER B on ifnull(a.CustomerID,'')=b.Customerid "+
                    " where (julianday(A.PayDate)-julianday('%s')) >=0 and (julianday('%s')-julianday(A.PayDate)) >=0 order by PayDate desc",fday,tDay);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            if (cursor.moveToFirst()) {
                do {
                    SM_CustomerPay oPay = new SM_CustomerPay();
                    oPay.setPayID(cursor.getString(cursor.getColumnIndex("PayID")));
                    oPay.setPayCode(cursor.getString(cursor.getColumnIndex("PayCode")));
                    oPay.setCustomerID(cursor.getString(cursor.getColumnIndex("CustomerID")));
                    oPay.setCustomerName(cursor.getString(cursor.getColumnIndex("CustomerName")));
                    oPay.setCustomerCode(cursor.getString(cursor.getColumnIndex("CustomerCode")));

                    oPay.setPayDate(cursor.getString(cursor.getColumnIndex("PayDate")));
                    oPay.setPayName(cursor.getString(cursor.getColumnIndex("PayName")));
                    oPay.setPayNotes(cursor.getString(cursor.getColumnIndex("PayNotes")));
                    oPay.setPayMoney(cursor.getDouble(cursor.getColumnIndex("PayMoney")));
                    oPay.setPayPic(cursor.getString(cursor.getColumnIndex("PayPic")));
                    oPay.setPayStatus(cursor.getInt(cursor.getColumnIndex("PayStatus")));
                    if(cursor.getString(cursor.getColumnIndex("PayStatus"))!=null){
                        if(cursor.getString(cursor.getColumnIndex("PayStatus")).equalsIgnoreCase("0")){
                            oPay.setPayStatusDesc("Phiếu mới");
                        }else if(cursor.getString(cursor.getColumnIndex("PayStatus")).equalsIgnoreCase("1")){
                            oPay.setPayStatusDesc("Đã điều chỉnh");
                        }else if(cursor.getString(cursor.getColumnIndex("PayStatus")).equalsIgnoreCase("3")){
                            oPay.setPayStatusDesc("Đã hủy");
                        }else{
                            oPay.setPayStatusDesc("");
                        }
                    }

                    oPay.setLongitude(cursor.getDouble(cursor.getColumnIndex("Longitude")));
                    oPay.setLatitude(cursor.getDouble(cursor.getColumnIndex("Latitude")));
                    oPay.setLocationAddress(cursor.getString(cursor.getColumnIndex("LocationAddress")));
                    oPay.setPostDay(cursor.getString(cursor.getColumnIndex("PostDay")));
                    if(cursor.getString(cursor.getColumnIndex("IsPost"))!=null && cursor.getString(cursor.getColumnIndex("IsPost")).contains("1")){
                        oPay.setPost(true);
                    }else{
                        oPay.setPost(false);
                    }
                    lstPay.add(oPay);

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return lstPay;
        }catch (Exception ex){Log.d("ERR_LOAD_SM_PAY",ex.getMessage().toString());}
        return null;
    }

    public SM_CustomerPay getCustomerPay(String PayID) {
        try {
            String mSql=String.format("Select A.*,B.CustomerName,B.CustomerCode from SM_CUSTOMER_PAY A LEFT JOIN DM_CUSTOMER B on a.CustomerID=b.Customerid "+
                    " where A.PayID='%s' order by PayDate desc",PayID);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            SM_CustomerPay oPay = new SM_CustomerPay();
            if (cursor.moveToFirst()) {
                do {
                    oPay.setPayID(cursor.getString(cursor.getColumnIndex("PayID")));
                    oPay.setPayCode(cursor.getString(cursor.getColumnIndex("PayCode")));
                    oPay.setCustomerID(cursor.getString(cursor.getColumnIndex("CustomerID")));
                    oPay.setCustomerName(cursor.getString(cursor.getColumnIndex("CustomerName")));
                    oPay.setCustomerCode(cursor.getString(cursor.getColumnIndex("CustomerCode")));

                    oPay.setPayDate(cursor.getString(cursor.getColumnIndex("PayDate")));
                    oPay.setPayName(cursor.getString(cursor.getColumnIndex("PayName")));
                    oPay.setPayNotes(cursor.getString(cursor.getColumnIndex("PayNotes")));
                    oPay.setPayMoney(cursor.getDouble(cursor.getColumnIndex("PayMoney")));
                    oPay.setPayPic(cursor.getString(cursor.getColumnIndex("PayPic")));
                    oPay.setPayStatus(cursor.getInt(cursor.getColumnIndex("PayStatus")));
                    if(cursor.getString(cursor.getColumnIndex("PayStatus"))!=null){
                        if(cursor.getString(cursor.getColumnIndex("PayStatus")).equalsIgnoreCase("0")){
                            oPay.setPayStatusDesc("Phiếu mới");
                        }else if(cursor.getString(cursor.getColumnIndex("PayStatus")).equalsIgnoreCase("1")){
                            oPay.setPayStatusDesc("Đã điều chỉnh");
                        }else if(cursor.getString(cursor.getColumnIndex("PayStatus")).equalsIgnoreCase("3")){
                            oPay.setPayStatusDesc("Đã hủy");
                        }else{
                            oPay.setPayStatusDesc("");
                        }
                    }

                    oPay.setLongitude(cursor.getDouble(cursor.getColumnIndex("Longitude")));
                    oPay.setLatitude(cursor.getDouble(cursor.getColumnIndex("Latitude")));
                    oPay.setLocationAddress(cursor.getString(cursor.getColumnIndex("LocationAddress")));
                    oPay.setPostDay(cursor.getString(cursor.getColumnIndex("PostDay")));
                    if(cursor.getString(cursor.getColumnIndex("IsPost"))!=null && cursor.getString(cursor.getColumnIndex("IsPost")).contains("1")){
                        oPay.setPost(true);
                    }else{
                        oPay.setPost(false);
                    }
                    break;
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return  oPay;
        }catch (Exception ex){Log.d("ERR_LOAD_SM_ORDER",ex.getMessage().toString());}
        return null;
    }

    public String addCustomerPay(SM_CustomerPay oPay){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int iSq = 1;

            String PayID=getPayID(oPay.getPayDate(),oPay.getCustomerID());
            if(PayID!=""){
                if(oPay.getPayID().isEmpty()|| oPay.getPayID()==null){
                    oPay.setPayID(PayID);
                }
            }
            iSq=getSizePay(oPay.getPayID());
            if (iSq<=0) {
                ContentValues values = new ContentValues();
                values.put("PayID", oPay.getPayID());
                values.put("PayCode", oPay.getPayCode());
                values.put("PayDate", oPay.getPayDate());

                values.put("PayName", oPay.getPayName());
                values.put("PayMoney",oPay.getPayMoney());
                values.put("PayNotes", oPay.getPayNotes());
                values.put("PayStatus", oPay.getPayStatus());
                values.put("PayPic", oPay.getPayPic());
                values.put("CustomerID", oPay.getCustomerID());

                values.put("Longitude",oPay.getLongitude());
                values.put("Latitude", oPay.getLatitude());
                values.put("LocationAddress", oPay.getLocationAddress());
                values.put("IsPost", oPay.getPost());
                values.put("PostDay", oPay.getPostDay());

                db.insert("SM_CUSTOMER_PAY", null, values);
            }else{
                ContentValues values = new ContentValues();
                values.put("PayCode", oPay.getPayCode());
                values.put("PayDate", oPay.getPayDate());

                values.put("PayName", oPay.getPayName());
                values.put("PayMoney",oPay.getPayMoney());
                values.put("PayNotes", oPay.getPayNotes());
                values.put("PayStatus", oPay.getPayStatus());
                values.put("PayPic", oPay.getPayPic());
                values.put("CustomerID", oPay.getCustomerID());

                values.put("Longitude",oPay.getLongitude());
                values.put("Latitude", oPay.getLatitude());
                values.put("LocationAddress", oPay.getLocationAddress());
                values.put("IsPost", oPay.getPost());
                values.put("PostDay", oPay.getPostDay());
                db.update("SM_CUSTOMER_PAY",values,"PayID=?" ,new String[] {String.valueOf(oPay.getPayID())});
            }
            db.close();
            return "";
        }catch (Exception e){
            return  "ERR:"+e.getMessage();
        }
    }

    public boolean editCustomerPay(SM_CustomerPay oPay){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("PayCode", oPay.getPayCode());
            values.put("PayDate", oPay.getPayDate());

            values.put("PayName", oPay.getPayName());
            values.put("PayMoney",oPay.getPayMoney());
            values.put("PayNotes", oPay.getPayNotes());
            values.put("PayStatus", oPay.getPayStatus());
            values.put("PayPic", oPay.getPayPic());
            values.put("CustomerID", oPay.getCustomerID());

            values.put("Longitude",oPay.getLongitude());
            values.put("Latitude", oPay.getLatitude());
            values.put("LocationAddress", oPay.getLocationAddress());
            values.put("IsPost", oPay.getPost());
            values.put("PostDay", oPay.getPostDay());
            db.update("SM_CUSTOMER_PAY",values,"PayID=?" ,new String[] {String.valueOf(oPay.getPayID())});
            db.close();
            return true;
        }catch (Exception e){Log.v("UDP_SM_ORDER_ERR",e.getMessage()); return  false;}
    }

    public boolean editCustomerPayStatus(SM_CustomerPay oPay){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("PayStatus", oPay.getPayStatus());
            values.put("IsPost", oPay.getPost());
            values.put("PostDay", oPay.getPostDay());
            db.update("SM_CUSTOMER_PAY",values,"PayID=?" ,new String[] {String.valueOf(oPay.getPayID())});
            db.close();
            return true;
        }catch (Exception e){Log.v("UDP_SM_PAY_ERR",e.getMessage()); return  false;}
    }



    public boolean delCustomerPay(SM_CustomerPay oPay){
        try {
            SQLiteDatabase db = getWritableDatabase();
            try {
                String mSql=String.format("delete from SM_CUSTOMER_PAY where PayID='%s'",oPay.getPayID());
                db.execSQL(mSql);
            }catch (Exception ex){
                return  false;
            }
            return  true;
        }catch (Exception ex){return false;}
    }

    /* REPORT TECH */
    public boolean getIsStatus(String mToday){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM SM_REPORT_TECH WHERE IsStatus<=3",null);
            int iSq= cursor.getCount();
            cursor.close();
            if (iSq>0){return  true;}else{return  false;}
        }catch(Exception ex){return false;}
    }
    private String getReportTechId(String ReportDate){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM SM_REPORT_TECH WHERE (julianday(ReportDay)-julianday('%s')=0) ", new String[]{ReportDate});
            String ReportTechId="";
            if (cursor.moveToFirst()) {
                do {
                    ReportTechId=cursor.getString(cursor.getColumnIndex("ReportTechID"));
                    break;
                } while (cursor.moveToNext());
            }
            cursor.close();
            return ReportTechId;
        }catch(Exception ex){return  "";}
    }
    public int getSizeReportTech(String ReportTechID){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM SM_REPORT_TECH WHERE ReportTechID=?", new String[]{ReportTechID});
            int iSq= cursor.getCount();
            cursor.close();
            return  iSq;
        }catch(Exception ex){return -1;}
    }

    public void CleanReportTech(int iIntervalDay) {
        try {
            SQLiteDatabase db = getWritableDatabase();

            String mSql = String.format("delete from SM_REPORT_TECH where ReportTechID in(select ReportTechID from SM_REPORT_TECH where julianday('now')-julianday(ReportDay)>%s)", iIntervalDay);
            db.execSQL(mSql);

            mSql = String.format("delete from SM_REPORT_TECH where julianday('now')-julianday(ReportDay)>%s", iIntervalDay);
            db.execSQL(mSql);
        } catch (Exception ex) {
        }
    }
    public List<SM_ReportTech> getAllReportTech(String fday, String tDay) {
        try {
            List<SM_ReportTech> lst = new ArrayList<SM_ReportTech>();
            String mSql=String.format("Select A.* from SM_REPORT_TECH A"+
                    " where (julianday(A.ReportDay)-julianday('%s')) >=0 and (julianday('%s')-julianday(A.ReportDay)) >=0 order by A.ReportDay desc",fday,tDay);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            if (cursor.moveToFirst()) {
                do {
                    SM_ReportTech oRptTech = new SM_ReportTech();
                    oRptTech.setReportTechId(cursor.getString(cursor.getColumnIndex("ReportTechID")));
                    oRptTech.setReportCode(cursor.getString(cursor.getColumnIndex("ReportCode")));
                    oRptTech.setReportName(cursor.getString(cursor.getColumnIndex("ReportName")));
                    oRptTech.setReportDate(cursor.getString(cursor.getColumnIndex("ReportDay")));
                    oRptTech.setLongtitude(cursor.getFloat(cursor.getColumnIndex("Longitude")));
                    oRptTech.setLatitude(cursor.getFloat(cursor.getColumnIndex("Latitude")));
                    oRptTech.setLocationAddress(cursor.getString(cursor.getColumnIndex("LocationAddress")));
                    oRptTech.setReceiverList(cursor.getString(cursor.getColumnIndex("ReceiverList")));
                    oRptTech.setNotes(cursor.getString(cursor.getColumnIndex("Notes")));
                    String isStatus = cursor.getString(cursor.getColumnIndex("IsStatus"));
                    if(isStatus != null)
                    {
                        if(isStatus.equalsIgnoreCase("0"))
                        {
                            oRptTech.setIsStatus("Phiếu mới");
                        }
                        else if(isStatus.equalsIgnoreCase("1"))
                        {
                            oRptTech.setIsStatus("Đã điều chỉnh");
                        }
                        else if(isStatus.equalsIgnoreCase("3"))
                        {
                            oRptTech.setIsStatus("Đã hủy");
                        }
                        else
                        {
                            oRptTech.setIsStatus("");
                        }

                    }
                    String isPost = cursor.getString(cursor.getColumnIndex("IsPost"));
                    if(isPost.equalsIgnoreCase("1"))
                    {
                        oRptTech.setPost(true);
                    }else{
                        oRptTech.setPost(false);
                    }
                    oRptTech.setPostDate(cursor.getString(cursor.getColumnIndex("PostDay")));

                    lst.add(oRptTech);

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return lst;
        }catch (Exception ex){Log.d("ERR_LOAD_SM_PAY",ex.getMessage().toString());}
        return null;
    }

    public SM_ReportTech getReportTechById(String reportTechId)
    {
        try {
            String mSql=String.format("Select A.* from SM_REPORT_TECH A "+
                    " where A.ReportTechID='%s' order by ReportDay desc",reportTechId);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            SM_ReportTech oRptTech = new SM_ReportTech();
            if (cursor.moveToFirst()) {
                do {
                    oRptTech.setReportTechId(cursor.getString(cursor.getColumnIndex("ReportTechID")));
                    oRptTech.setReportCode(cursor.getString(cursor.getColumnIndex("ReportCode")));
                    oRptTech.setReportName(cursor.getString(cursor.getColumnIndex("ReportName")));
                    oRptTech.setReportDate(cursor.getString(cursor.getColumnIndex("ReportDay")));
                    oRptTech.setLongtitude(cursor.getFloat(cursor.getColumnIndex("Longitude")));
                    oRptTech.setLatitude(cursor.getFloat(cursor.getColumnIndex("Latitude")));
                    oRptTech.setLocationAddress(cursor.getString(cursor.getColumnIndex("LocationAddress")));
                    oRptTech.setReceiverList(cursor.getString(cursor.getColumnIndex("ReceiverList")));
                    oRptTech.setNotes(cursor.getString(cursor.getColumnIndex("Notes")));
                    String isStatus = cursor.getString(cursor.getColumnIndex("IsStatus"));
                    if(isStatus != null)
                    {
                        if(isStatus.equalsIgnoreCase("0"))
                        {
                            oRptTech.setIsStatus("Phiếu mới");
                        }
                        else if(isStatus.equalsIgnoreCase("1"))
                        {
                            oRptTech.setIsStatus("Đã điều chỉnh");
                        }
                        else if(isStatus.equalsIgnoreCase("3"))
                        {
                            oRptTech.setIsStatus("Đã hủy");
                        }
                        else
                        {
                            oRptTech.setIsStatus("");
                        }
                    }
                    String isPost = cursor.getString(cursor.getColumnIndex("IsPost"));
                    if(isPost.equalsIgnoreCase("1"))
                    {
                        oRptTech.setPost(true);
                    }else{
                        oRptTech.setPost(false);
                    }
                    oRptTech.setPostDate(cursor.getString(cursor.getColumnIndex("PostDay")));

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return oRptTech;
        }catch (Exception ex){
            Log.d("ERR_LOAD_SM_PAY",ex.getMessage().toString());
        }
        return null;
    }

    public boolean editReportTech(SM_ReportTech oRptTech){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("ReportCode", oRptTech.getReportCode());
            values.put("ReportName", oRptTech.getReportName());

            values.put("ReportDay", oRptTech.getReportDate());
            values.put("Longitude", oRptTech.getLongtitude());
            values.put("Latitude", oRptTech.getLatitude());
            values.put("LocationAddress", oRptTech.getLocationAddress());
            values.put("ReceiverList", oRptTech.getReceiverList());
            values.put("Notes", oRptTech.getNotes());

            values.put("IsStatus", oRptTech.getIsStatus());
            values.put("IsPost", oRptTech.isPost());
            values.put("PostDay", oRptTech.getPostDate());
            //db.update("SM_REPORT_TECH",values,"ReportCode=?" ,new String[] {String.valueOf(oRptTech.getReportCode())});
            db.update("SM_REPORT_TECH",values,"ReportTechID=?" ,new String[] {String.valueOf(oRptTech.getReportTechId())});
            db.close();
            return true;
        }catch (Exception e){Log.v("UDP_SM_RPT_TECH_ERR",e.getMessage()); return  false;}
    }

    public String addReportTech(SM_ReportTech oRptTech){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int iSq = 1;

            String ReportTechID=getReportTechId(oRptTech.getReportDate());
            if(ReportTechID!=""){
                if(oRptTech.getReportTechId().isEmpty()|| oRptTech.getReportTechId()==null){
                    oRptTech.setReportTechId(ReportTechID);
                }
            }
            iSq=getSizeReportTech(oRptTech.getReportTechId());
            if (iSq<=0) {
                ContentValues values = new ContentValues();
                values.put("ReportTechID", oRptTech.getReportTechId());
                values.put("ReportCode", oRptTech.getReportCode());
                values.put("ReportName", oRptTech.getReportName());

                values.put("ReportDay", oRptTech.getReportDate());
                values.put("Longitude", oRptTech.getLongtitude());
                values.put("Latitude", oRptTech.getLatitude());
                values.put("LocationAddress", oRptTech.getLocationAddress());
                values.put("ReceiverList", oRptTech.getReceiverList());
                values.put("Notes", oRptTech.getNotes());

                values.put("IsStatus", oRptTech.getIsStatus());
                values.put("IsPost", oRptTech.isPost());
                values.put("PostDay", oRptTech.getPostDate());

                db.insert("SM_REPORT_TECH", null, values);
            }else{
                ContentValues values = new ContentValues();
                values.put("ReportCode", oRptTech.getReportCode());
                values.put("ReportName", oRptTech.getReportName());

                values.put("ReportDay", oRptTech.getReportDate());
                values.put("Longitude", oRptTech.getLongtitude());
                values.put("Latitude", oRptTech.getLatitude());
                values.put("LocationAddress", oRptTech.getLocationAddress());
                values.put("ReceiverList", oRptTech.getReceiverList());
                values.put("Notes", oRptTech.getNotes());

                values.put("IsStatus", oRptTech.getIsStatus());
                values.put("IsPost", oRptTech.isPost());
                values.put("PostDay", oRptTech.getPostDate());
                db.update("SM_REPORT_TECH",values,"ReportTechID=?" ,new String[] {String.valueOf(oRptTech.getReportTechId())});
            }
            db.close();
            return "";
        }catch (Exception e){
            return  "ERR:"+e.getMessage();
        }
    }

    /* END REPORT TECH */

    /* REPORT TECH THỊ TRƯỜNG */
    private String getReportTechMarketID(String ReportTechID){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT A.* FROM SM_REPORT_TECH_MARKET A LEFT JOIN SM_REPORT_TECH B ON A.ReportTechID = B.ReportTechID WHERE B.ReportTechID=? ", new String[]{ReportTechID});
            String MarketID="";
            if (cursor.moveToFirst()) {
                do {
                    MarketID=cursor.getString(cursor.getColumnIndex("MarketID"));
                    break;
                } while (cursor.moveToNext());
            }
            cursor.close();
            return MarketID;
        }catch(Exception ex){return  "";}
    }
    public int getSizeReportTechMarket(String MarketID){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM SM_REPORT_TECH_MARKET WHERE MarketID=?", new String[]{MarketID});
            int iSq= cursor.getCount();
            cursor.close();
            return  iSq;
        }catch(Exception ex){return -1;}
    }

    public void CleanReportTechMarket(int iIntervalDay) {
        try {
            SQLiteDatabase db = getWritableDatabase();

            String mSql = String.format("delete from SM_REPORT_TECH_MARKET where ReportTechID in(select ReportTechID from SM_REPORT_TECH where julianday('now')-julianday(ReportDay)>%s)", iIntervalDay);
            db.execSQL(mSql);

        } catch (Exception ex) {
        }
    }
    public List<SM_ReportTechMarket> getAllReportTechMarket(String mReportTechId) {
        try {
            List<SM_ReportTechMarket> lst = new ArrayList<SM_ReportTechMarket>();
            String mSql=String.format("Select A.* from SM_REPORT_TECH_MARKET A LEFT JOIN SM_REPORT_TECH B ON A.ReportTechID = B.ReportTechID"+
                    " where A.ReportTechID='%s' order by B.ReportDay desc", mReportTechId);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            if (cursor.moveToFirst()) {
                do {
                    SM_ReportTechMarket oRptTechMarket = new SM_ReportTechMarket();
                    oRptTechMarket.setMarketId(cursor.getString(cursor.getColumnIndex("MarketID")));
                    oRptTechMarket.setReportTechId(cursor.getString(cursor.getColumnIndex("ReportTechID")));
                    oRptTechMarket.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                    oRptTechMarket.setNotes(cursor.getString(cursor.getColumnIndex("Notes")));
                    oRptTechMarket.setUsefull(cursor.getString(cursor.getColumnIndex("Useful")));
                    oRptTechMarket.setHarmful(cursor.getString(cursor.getColumnIndex("Harmful")));
                    lst.add(oRptTechMarket);

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return lst;
        }catch (Exception ex){Log.d("ERR_LOAD_TECH_MARKET",ex.getMessage().toString());}
        return null;
    }

    public SM_ReportTechMarket getReportTechMarketById(String MarketID)
    {
        try {
            String mSql=String.format("Select A.* from SM_REPORT_TECH_MARKET A LEFT JOIN SM_REPORT_TECH B ON A.ReportTechID = B.ReportTechID "+
                    " where A.MarketID='%s' order by B.ReportDay desc",MarketID);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            SM_ReportTechMarket oRptTechMarket = new SM_ReportTechMarket();
            if (cursor.moveToFirst()) {
                do {
                    oRptTechMarket.setMarketId(cursor.getString(cursor.getColumnIndex("MarketID")));
                    oRptTechMarket.setReportTechId(cursor.getString(cursor.getColumnIndex("ReportTechID")));
                    oRptTechMarket.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                    oRptTechMarket.setNotes(cursor.getString(cursor.getColumnIndex("Notes")));
                    oRptTechMarket.setUsefull(cursor.getString(cursor.getColumnIndex("Useful")));
                    oRptTechMarket.setHarmful(cursor.getString(cursor.getColumnIndex("Harmful")));

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return oRptTechMarket;
        }catch (Exception ex){
            Log.d("ERR_LOAD_TECH_MARKET",ex.getMessage().toString());
        }
        return null;
    }

    public String addReportTechMarket(SM_ReportTechMarket oRptTechMarket){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int iSq = 1;

            String MarketID=getReportTechMarketID(oRptTechMarket.getReportTechId());
            if(MarketID!=""){
                if(oRptTechMarket.getMarketId().isEmpty()|| oRptTechMarket.getMarketId()==null){
                    oRptTechMarket.setMarketId(MarketID);
                }
            }
            iSq=getSizeReportTechMarket(oRptTechMarket.getMarketId());
            if (iSq<=0) {
                ContentValues values = new ContentValues();
                values.put("MarketID", oRptTechMarket.getMarketId());
                values.put("ReportTechID", oRptTechMarket.getReportTechId());
                values.put("Title", oRptTechMarket.getTitle());
                values.put("Notes", oRptTechMarket.getNotes());
                values.put("Useful", oRptTechMarket.getUsefull());
                values.put("Harmful", oRptTechMarket.getHarmful());
                db.insert("SM_REPORT_TECH_MARKET", null, values);
            }else{
                ContentValues values = new ContentValues();
                values.put("ReportTechID", oRptTechMarket.getReportTechId());
                values.put("Title", oRptTechMarket.getTitle());
                values.put("Notes", oRptTechMarket.getNotes());
                values.put("Useful", oRptTechMarket.getUsefull());
                values.put("Harmful", oRptTechMarket.getHarmful());
                db.update("SM_REPORT_TECH_MARKET",values,"MarketID=?" ,new String[] {String.valueOf(oRptTechMarket.getMarketId())});
            }
            db.close();
            return "";
        }catch (Exception e){
            return  "ERR:"+e.getMessage();
        }
    }

    /* END REPORT TECH THI TRUONG */

    /* REPORT TECH DỊCH BỆNH */
    private String getReportTechDiseaseID(String ReportTechID){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT A.* FROM SM_REPORT_TECH_DISEASE A LEFT JOIN SM_REPORT_TECH B ON A.ReportTechID = B.ReportTechID WHERE B.ReportTechID=? ", new String[]{ReportTechID});
            String DiseaseID="";
            if (cursor.moveToFirst()) {
                do {
                    DiseaseID=cursor.getString(cursor.getColumnIndex("DiseaseID"));
                    break;
                } while (cursor.moveToNext());
            }
            cursor.close();
            return DiseaseID;
        }catch(Exception ex){return  "";}
    }
    public int getSizeReportTechDisease(String DiseaseID){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM SM_REPORT_TECH_DISEASE WHERE DiseaseID=?", new String[]{DiseaseID});
            int iSq= cursor.getCount();
            cursor.close();
            return  iSq;
        }catch(Exception ex){return -1;}
    }

    public void CleanReportTechDisease(int iIntervalDay) {
        try {
            SQLiteDatabase db = getWritableDatabase();

            String mSql = String.format("delete from SM_REPORT_TECH_DISEASE where ReportTechID in(select ReportTechID from SM_REPORT_TECH where julianday('now')-julianday(ReportDay)>%s)", iIntervalDay);
            db.execSQL(mSql);

        } catch (Exception ex) {
        }
    }
    public List<SM_ReportTechDisease> getAllReportTechDisease(String mReportTechId) {
        try {
            List<SM_ReportTechDisease> lst = new ArrayList<SM_ReportTechDisease>();
            String mSql=String.format("Select A.* from SM_REPORT_TECH_DISEASE A LEFT JOIN SM_REPORT_TECH B ON A.ReportTechID = B.ReportTechID"+
                    " where A.ReportTechID='%s' order by B.ReportDay desc", mReportTechId);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            if (cursor.moveToFirst()) {
                do {
                    SM_ReportTechDisease oRptTechDisease = new SM_ReportTechDisease();
                    oRptTechDisease.setDiseaseId(cursor.getString(cursor.getColumnIndex("DiseaseID")));
                    oRptTechDisease.setReportTechId(cursor.getString(cursor.getColumnIndex("ReportTechID")));
                    oRptTechDisease.setTreeCode(cursor.getString(cursor.getColumnIndex("TreeCode")));
                    oRptTechDisease.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                    oRptTechDisease.setAcreage(cursor.getFloat(cursor.getColumnIndex("Acreage")));
                    oRptTechDisease.setDisease(cursor.getString(cursor.getColumnIndex("Disease")));
                    oRptTechDisease.setPrice(cursor.getFloat(cursor.getColumnIndex("Price")));
                    oRptTechDisease.setNotes(cursor.getString(cursor.getColumnIndex("Notes")));
                    lst.add(oRptTechDisease);

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return lst;
        }catch (Exception ex){Log.d("ERR_LOAD_TECH_DISEASE",ex.getMessage().toString());}
        return null;
    }

    public SM_ReportTechDisease getReportTechDiseaseById(String DiseaseID)
    {
        try {
            String mSql=String.format("Select A.* from SM_REPORT_TECH_DISEASE A LEFT JOIN SM_REPORT_TECH B ON A.ReportTechID = B.ReportTechID "+
                    " where A.DiseaseID='%s' order by B.ReportDay desc",DiseaseID);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            SM_ReportTechDisease oRptTechDisease = new SM_ReportTechDisease();
            if (cursor.moveToFirst()) {
                do {
                    oRptTechDisease.setDiseaseId(cursor.getString(cursor.getColumnIndex("DiseaseID")));
                    oRptTechDisease.setReportTechId(cursor.getString(cursor.getColumnIndex("ReportTechID")));
                    oRptTechDisease.setTreeCode(cursor.getString(cursor.getColumnIndex("TreeCode")));
                    oRptTechDisease.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                    oRptTechDisease.setAcreage(cursor.getFloat(cursor.getColumnIndex("Acreage")));
                    oRptTechDisease.setDisease(cursor.getString(cursor.getColumnIndex("Disease")));
                    oRptTechDisease.setPrice(cursor.getFloat(cursor.getColumnIndex("Price")));
                    oRptTechDisease.setNotes(cursor.getString(cursor.getColumnIndex("Notes")));

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return oRptTechDisease;
        }catch (Exception ex){
            Log.d("ERR_LOAD_TECH_DISEASE",ex.getMessage().toString());
        }
        return null;
    }

    public String addReportTechDisease(SM_ReportTechDisease oRptTechDisease){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int iSq = 1;

            String DiseaseID=getReportTechDiseaseID(oRptTechDisease.getReportTechId());
            if(DiseaseID!=""){
                if(oRptTechDisease.getDiseaseId().isEmpty()|| oRptTechDisease.getDiseaseId()==null){
                    oRptTechDisease.setDiseaseId(DiseaseID);
                }
            }
            iSq=getSizeReportTechDisease(oRptTechDisease.getDiseaseId());
            if (iSq<=0) {
                ContentValues values = new ContentValues();
                values.put("DiseaseID", oRptTechDisease.getDiseaseId());
                values.put("ReportTechID", oRptTechDisease.getReportTechId());
                values.put("TreeCode", oRptTechDisease.getTreeCode());
                values.put("Title", oRptTechDisease.getTitle());
                values.put("Acreage", oRptTechDisease.getAcreage());
                values.put("Disease", oRptTechDisease.getDisease());
                values.put("Price", oRptTechDisease.getPrice());
                values.put("Notes", oRptTechDisease.getNotes());
                db.insert("SM_REPORT_TECH_DISEASE", null, values);
            }else{
                ContentValues values = new ContentValues();
                values.put("ReportTechID", oRptTechDisease.getReportTechId());
                values.put("TreeCode", oRptTechDisease.getTreeCode());
                values.put("Title", oRptTechDisease.getTitle());
                values.put("Acreage", oRptTechDisease.getAcreage());
                values.put("Disease", oRptTechDisease.getDisease());
                values.put("Price", oRptTechDisease.getPrice());
                values.put("Notes", oRptTechDisease.getNotes());
                db.update("SM_REPORT_TECH_DISEASE",values,"DiseaseID=?" ,new String[] {String.valueOf(oRptTechDisease.getDiseaseId())});
            }
            db.close();
            return "";
        }catch (Exception e){
            return  "ERR:"+e.getMessage();
        }
    }

    /* END REPORT TECH DỊCH BỆNH */

    /* REPORT TECH ĐỐI THỦ */
    private String getReportTechCompetitorID(String ReportTechID){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT A.* FROM SM_REPORT_TECH_COMPETITOR A LEFT JOIN SM_REPORT_TECH B ON A.ReportTechID = B.ReportTechID WHERE B.ReportTechID=? ", new String[]{ReportTechID});
            String CompetitorID="";
            if (cursor.moveToFirst()) {
                do {
                    CompetitorID=cursor.getString(cursor.getColumnIndex("CompetitorID"));
                    break;
                } while (cursor.moveToNext());
            }
            cursor.close();
            return CompetitorID;
        }catch(Exception ex){return  "";}
    }
    public int getSizeReportTechCompetitor(String CompetitorID){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM SM_REPORT_TECH_COMPETITOR WHERE CompetitorID=?", new String[]{CompetitorID});
            int iSq= cursor.getCount();
            cursor.close();
            return  iSq;
        }catch(Exception ex){return -1;}
    }

    public void CleanReportTechCompetitor(int iIntervalDay) {
        try {
            SQLiteDatabase db = getWritableDatabase();

            String mSql = String.format("delete from SM_REPORT_TECH_COMPETITOR where ReportTechID in(select ReportTechID from SM_REPORT_TECH where julianday('now')-julianday(ReportDay)>%s)", iIntervalDay);
            db.execSQL(mSql);

        } catch (Exception ex) {
        }
    }
    public List<SM_ReportTechCompetitor> getAllReportTechCompetitor(String mReportTechId) {
        try {
            List<SM_ReportTechCompetitor> lst = new ArrayList<SM_ReportTechCompetitor>();
            String mSql=String.format("Select A.* from SM_REPORT_TECH_COMPETITOR A LEFT JOIN SM_REPORT_TECH B ON A.ReportTechID = B.ReportTechID"+
                    " where A.ReportTechID='%s' order by B.ReportDay desc", mReportTechId);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            if (cursor.moveToFirst()) {
                do {
                    SM_ReportTechCompetitor oRptTechCompetitor = new SM_ReportTechCompetitor();
                    oRptTechCompetitor.setCompetitorId(cursor.getString(cursor.getColumnIndex("CompetitorID")));
                    oRptTechCompetitor.setReportTechId(cursor.getString(cursor.getColumnIndex("ReportTechID")));
                    oRptTechCompetitor.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                    oRptTechCompetitor.setNotes(cursor.getString(cursor.getColumnIndex("Notes")));
                    oRptTechCompetitor.setUseful(cursor.getString(cursor.getColumnIndex("Useful")));
                    oRptTechCompetitor.setHarmful(cursor.getString(cursor.getColumnIndex("Harmful")));
                    lst.add(oRptTechCompetitor);

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return lst;
        }catch (Exception ex){Log.d("ERR_LOAD_TECHCOMPETITOR",ex.getMessage().toString());}
        return null;
    }

    public SM_ReportTechCompetitor getReportTechCompetitorById(String CompetitorID)
    {
        try {
            String mSql=String.format("Select A.* from SM_REPORT_TECH_COMPETITOR A LEFT JOIN SM_REPORT_TECH B ON A.ReportTechID = B.ReportTechID "+
                    " where A.DiseaseID='%s' order by B.ReportDay desc",CompetitorID);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            SM_ReportTechCompetitor oRptTechCompetitor = new SM_ReportTechCompetitor();
            if (cursor.moveToFirst()) {
                do {
                    oRptTechCompetitor.setCompetitorId(cursor.getString(cursor.getColumnIndex("CompetitorID")));
                    oRptTechCompetitor.setReportTechId(cursor.getString(cursor.getColumnIndex("ReportTechID")));
                    oRptTechCompetitor.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                    oRptTechCompetitor.setNotes(cursor.getString(cursor.getColumnIndex("Notes")));
                    oRptTechCompetitor.setUseful(cursor.getString(cursor.getColumnIndex("Useful")));
                    oRptTechCompetitor.setHarmful(cursor.getString(cursor.getColumnIndex("Harmful")));

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return oRptTechCompetitor;
        }catch (Exception ex){
            Log.d("ERR_LOAD_TECHCOMPETITOR",ex.getMessage().toString());
        }
        return null;
    }

    public String addReportTechCompetitor(SM_ReportTechCompetitor oRptTechCompetotitor){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int iSq = 1;

            String CompetitorID=getReportTechCompetitorID(oRptTechCompetotitor.getReportTechId());
            if(CompetitorID!=""){
                if(oRptTechCompetotitor.getCompetitorId().isEmpty()|| oRptTechCompetotitor.getCompetitorId()==null){
                    oRptTechCompetotitor.setCompetitorId(CompetitorID);
                }
            }
            iSq=getSizeReportTechCompetitor(oRptTechCompetotitor.getCompetitorId());
            if (iSq<=0) {
                ContentValues values = new ContentValues();
                values.put("CompetitorID", oRptTechCompetotitor.getCompetitorId());
                values.put("ReportTechID", oRptTechCompetotitor.getReportTechId());
                values.put("Title", oRptTechCompetotitor.getTitle());
                values.put("Notes", oRptTechCompetotitor.getNotes());
                values.put("Useful", oRptTechCompetotitor.getUseful());
                values.put("Harmful", oRptTechCompetotitor.getHarmful());
                db.insert("SM_REPORT_TECH_COMPETITOR", null, values);
            }else{
                ContentValues values = new ContentValues();
                values.put("ReportTechID", oRptTechCompetotitor.getReportTechId());
                values.put("Title", oRptTechCompetotitor.getTitle());
                values.put("Notes", oRptTechCompetotitor.getNotes());
                values.put("Useful", oRptTechCompetotitor.getUseful());
                values.put("Harmful", oRptTechCompetotitor.getHarmful());
                db.update("SM_REPORT_TECH_COMPETITOR",values,"CompetitorID=?" ,new String[] {String.valueOf(oRptTechCompetotitor.getCompetitorId())});
            }
            db.close();
            return "";
        }catch (Exception e){
            return  "ERR:"+e.getMessage();
        }
    }

    /* END REPORT TECH ĐỐI THỦ */

    /* REPORT TECH HOẠT ĐỘNG */
    private String getReportTechActivityID(String ReportTechID){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT A.* FROM SM_REPORT_TECH_ACTIVITIE A LEFT JOIN SM_REPORT_TECH B ON A.ReportTechID = B.ReportTechID WHERE B.ReportTechID=? ", new String[]{ReportTechID});
            String ActivitieID="";
            if (cursor.moveToFirst()) {
                do {
                    ActivitieID=cursor.getString(cursor.getColumnIndex("ActivitieID"));
                    break;
                } while (cursor.moveToNext());
            }
            cursor.close();
            return ActivitieID;
        }catch(Exception ex){return  "";}
    }
    public int getSizeReportTechActivity(String ActivitieID){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM SM_REPORT_TECH_ACTIVITIE WHERE ActivitieID=?", new String[]{ActivitieID});
            int iSq= cursor.getCount();
            cursor.close();
            return  iSq;
        }catch(Exception ex){return -1;}
    }

    public void CleanReportTechActivity(int iIntervalDay) {
        try {
            SQLiteDatabase db = getWritableDatabase();

            String mSql = String.format("delete from SM_REPORT_TECH_ACTIVITIE where ReportTechID in(select ReportTechID from SM_REPORT_TECH where julianday('now')-julianday(ReportDay)>%s)", iIntervalDay);
            db.execSQL(mSql);

        } catch (Exception ex) {
        }
    }
    public List<SM_ReportTechActivity> getAllReportTechActivity(String mReportTechId, Integer type) {
        try {
            List<SM_ReportTechActivity> lst = new ArrayList<SM_ReportTechActivity>();
            String mSql=String.format("Select A.* from SM_REPORT_TECH_ACTIVITIE A LEFT JOIN SM_REPORT_TECH B ON A.ReportTechID = B.ReportTechID"+
                    " where A.ReportTechId='%s' and A.IsType=%d order by B.ReportDay desc", mReportTechId, type);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            if (cursor.moveToFirst()) {
                do {
                    SM_ReportTechActivity oRptTechActivity = new SM_ReportTechActivity();
                    oRptTechActivity.setActivitieId(cursor.getString(cursor.getColumnIndex("ActivitieID")));
                    oRptTechActivity.setReportTechId(cursor.getString(cursor.getColumnIndex("ReportTechID")));
                    oRptTechActivity.setIsType(cursor.getInt(cursor.getColumnIndex("IsType")));
                    oRptTechActivity.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                    oRptTechActivity.setNotes(cursor.getString(cursor.getColumnIndex("Notes")));
                    oRptTechActivity.setAchievement(cursor.getString(cursor.getColumnIndex("Achievement")));
                    lst.add(oRptTechActivity);

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return lst;
        }catch (Exception ex){Log.d("ERR_LOAD_TECH_ACTIVITY",ex.getMessage().toString());}
        return null;
    }

    public SM_ReportTechActivity getReportTechActivityById(String ActivitieID)
    {
        try {
            String mSql=String.format("Select A.* from SM_REPORT_TECH_ACTIVITIE A LEFT JOIN SM_REPORT_TECH B ON A.ReportTechID = B.ReportTechID "+
                    " where A.DiseaseID='%s' order by B.ReportDay desc",ActivitieID);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            SM_ReportTechActivity oRptTechActivity = new SM_ReportTechActivity();
            if (cursor.moveToFirst()) {
                do {
                    oRptTechActivity.setActivitieId(cursor.getString(cursor.getColumnIndex("ActivitieID")));
                    oRptTechActivity.setReportTechId(cursor.getString(cursor.getColumnIndex("ReportTechID")));
                    oRptTechActivity.setIsType(cursor.getInt(cursor.getColumnIndex("IsType")));
                    oRptTechActivity.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                    oRptTechActivity.setNotes(cursor.getString(cursor.getColumnIndex("Notes")));
                    oRptTechActivity.setAchievement(cursor.getString(cursor.getColumnIndex("Achievement")));

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return oRptTechActivity;
        }catch (Exception ex){
            Log.d("ERR_LOAD_TECH_ACTIVITY",ex.getMessage().toString());
        }
        return null;
    }

    public String addReportTechActivity(SM_ReportTechActivity oRptTechActivity){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int iSq = 1;

            String ActivitieID=getReportTechActivityID(oRptTechActivity.getReportTechId());
            if(ActivitieID!=""){
                if(oRptTechActivity.getActivitieId().isEmpty()|| oRptTechActivity.getActivitieId()==null){
                    oRptTechActivity.setActivitieId(ActivitieID);
                }
            }
            iSq=getSizeReportTechActivity(oRptTechActivity.getActivitieId());
            if (iSq<=0) {
                ContentValues values = new ContentValues();
                values.put("ActivitieID", oRptTechActivity.getActivitieId());
                values.put("ReportTechID", oRptTechActivity.getReportTechId());
                values.put("IsType", oRptTechActivity.getIsType());
                values.put("Title", oRptTechActivity.getTitle());
                values.put("Notes", oRptTechActivity.getNotes());
                values.put("Achievement", oRptTechActivity.getAchievement());
                db.insert("SM_REPORT_TECH_ACTIVITIE", null, values);
            }else{
                ContentValues values = new ContentValues();
                values.put("ReportTechID", oRptTechActivity.getReportTechId());
                values.put("IsType", oRptTechActivity.getIsType());
                values.put("Title", oRptTechActivity.getTitle());
                values.put("Notes", oRptTechActivity.getNotes());
                values.put("Achievement", oRptTechActivity.getAchievement());
                db.update("SM_REPORT_TECH_ACTIVITIE",values,"ActivitieID=?" ,new String[] {String.valueOf(oRptTechActivity.getActivitieId())});
            }
            db.close();
            return "";
        }catch (Exception e){
            return  "ERR:"+e.getMessage();
        }
    }

    /* END REPORT TECH HOẠT ĐỘNG */

    public boolean delReportTech(String mReportTechId){
        try {
            SQLiteDatabase db = getWritableDatabase();
            String mSqlReportTech=String.format("delete from SM_REPORT_TECH where ReportTechID ='%s'",mReportTechId);
            String mSqlMarket=String.format("delete from SM_REPORT_TECH_MARKET where ReportTechID ='%s'",mReportTechId);
            String mSqlDisease=String.format("delete from SM_REPORT_TECH_DISEASE where ReportTechID ='%s'",mReportTechId);
            String mSqlCompetitor=String.format("delete from SM_REPORT_TECH_COMPETITOR where ReportTechID ='%s'",mReportTechId);
            String mSqlActivity=String.format("delete from SM_REPORT_TECH_ACTIVITIE where ReportTechID ='%s'",mReportTechId);

            try {
                db.execSQL(mSqlReportTech);
                db.execSQL(mSqlMarket);
                db.execSQL(mSqlDisease);
                db.execSQL(mSqlCompetitor);
                db.execSQL(mSqlActivity);

            }catch (Exception ex){
                Log.d("DEL_SM_ODT",ex.getMessage());
                return  false;
            }
            return  true;
        }catch (Exception ex){return false;}
    }

    public boolean delReportTechDetail(String mReportTechId){
        try {
            SQLiteDatabase db = getWritableDatabase();
            String mSqlMarket=String.format("delete from SM_REPORT_TECH_MARKET where ReportTechID ='%s'",mReportTechId);
            String mSqlDisease=String.format("delete from SM_REPORT_TECH_DISEASE where ReportTechID ='%s'",mReportTechId);
            String mSqlCompetitor=String.format("delete from SM_REPORT_TECH_COMPETITOR where ReportTechID ='%s'",mReportTechId);
            String mSqlActivity=String.format("delete from SM_REPORT_TECH_ACTIVITIE where ReportTechID ='%s'",mReportTechId);

            try {
                db.execSQL(mSqlMarket);
                db.execSQL(mSqlDisease);
                db.execSQL(mSqlCompetitor);
                db.execSQL(mSqlActivity);

            }catch (Exception ex){
                Log.d("DEL_SM_ODT",ex.getMessage());
                return  false;
            }
            return  true;
        }catch (Exception ex){return false;}
    }

    public List<DM_Tree_Disease> getListTreeDiseaseByTreeCode(String treeCode)
    { try {
            List<DM_Tree_Disease> lst = new ArrayList<>();
            String mSql=String.format("Select A.* from DM_TREE_DISEASE A LEFT JOIN DM_TREE B ON A.TreeID = B.TreeID"+
                    " where B.TreeCode='%s' ", treeCode);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            if (cursor.moveToFirst()) {
                do {
                    DM_Tree_Disease oTreeDisease = new DM_Tree_Disease();
                    oTreeDisease.setDiseaseID(cursor.getInt(cursor.getColumnIndex("DiseaseID")));
                    oTreeDisease.setTreeID(cursor.getInt(cursor.getColumnIndex("TreeID")));
                    oTreeDisease.setDiseaseCode(cursor.getString(cursor.getColumnIndex("DiseaseCode")));
                    oTreeDisease.setDiseaseName(cursor.getString(cursor.getColumnIndex("DiseaseName")));
                    lst.add(oTreeDisease);

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return lst;
        }catch (Exception ex){
            Log.d("GET_TREE_DISEASES",ex.getMessage());
        }
        return null;
    }

    public DM_Tree getTreeByCode(String treeCode)
    {
        try {
            String mSql=String.format("Select A.* from DM_TREE A where A.TreeCode='%s' ", treeCode);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            DM_Tree oTree = new DM_Tree();
            if (cursor.moveToFirst()) {
                do {
                    oTree.setTreeID(cursor.getInt(cursor.getColumnIndex("TreeID")));
                    oTree.setTreeCode(cursor.getString(cursor.getColumnIndex("TreeCode")));
                    oTree.setTreeGroupCode(cursor.getString(cursor.getColumnIndex("TreeGroupCode")));
                    oTree.setTreeName(cursor.getString(cursor.getColumnIndex("TreeName")));

                }while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return oTree;
        }catch (Exception ex){
            Log.d("GET_TREE", ex.getMessage());
        }
        return null;
    }

    public List<DM_Tree_Disease> getTreeDiseasesByCode(String[] DiseaseCode)
    {
        try {
            if(DiseaseCode.length < 0){
                return null;
            }
            String lstCode = "";
            for(int i = 0; i < DiseaseCode.length; i++){
                if(i > 0){
                    lstCode += ",";
                }
                lstCode +=  String.format("'%s'",DiseaseCode[i]);
            }
            String mSql=String.format("Select * from DM_TREE_DISEASE where DiseaseCode in (%s)", lstCode);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            List<DM_Tree_Disease> lst = new ArrayList<DM_Tree_Disease>();
            if (cursor.moveToFirst()) {
                do {
                    DM_Tree_Disease oTreeDisease = new DM_Tree_Disease();
                    oTreeDisease.setDiseaseID(cursor.getInt(cursor.getColumnIndex("DiseaseID")));
                    oTreeDisease.setTreeID(cursor.getInt(cursor.getColumnIndex("TreeID")));
                    oTreeDisease.setDiseaseCode(cursor.getString(cursor.getColumnIndex("DiseaseCode")));
                    oTreeDisease.setDiseaseName(cursor.getString(cursor.getColumnIndex("DiseaseName")));

                    lst.add(oTreeDisease);
                }while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return lst;
        }catch (Exception ex){
            Log.d("GET_TREE_DISEASE", ex.getMessage());
        }
        return null;
    }

    /* REPORT SALE REP */
    private String getReportSaleRepId(String ReportDate){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM SM_REPORT_SALEREP WHERE (julianday(ReportDay)-julianday('%s')=0) ", new String[]{ReportDate});
            String ReportSaleRepId="";
            if (cursor.moveToFirst()) {
                do {
                    ReportSaleRepId=cursor.getString(cursor.getColumnIndex("ReportSaleID"));
                    break;
                } while (cursor.moveToNext());
            }
            cursor.close();
            return ReportSaleRepId;
        }catch(Exception ex){return  "";}
    }

    public int getSizeReportSaleRep(String ReportSaleID){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM SM_REPORT_SALEREP WHERE ReportSaleID=?", new String[]{ReportSaleID});
            int iSq= cursor.getCount();
            cursor.close();
            return  iSq;
        }catch(Exception ex){return -1;}
    }

    public void CleanReportSaleRep(int iIntervalDay) {
        try {
            SQLiteDatabase db = getWritableDatabase();

            String mSql = String.format("delete from SM_REPORT_SALEREP where ReportSaleID in(select ReportSaleID from SM_REPORT_SALEREP where julianday('now')-julianday(ReportDay)>%s)", iIntervalDay);
            db.execSQL(mSql);

            mSql = String.format("delete from SM_REPORT_SALEREP where julianday('now')-julianday(ReportDay)>%s", iIntervalDay);
            db.execSQL(mSql);
        } catch (Exception ex) {
        }
    }

    public List<SM_ReportSaleRep> getAllReportSaleRep(String fday, String tDay) {
        try {
            List<SM_ReportSaleRep> lst = new ArrayList<SM_ReportSaleRep>();
            String mSql=String.format("Select A.* from SM_REPORT_SALEREP A"+
                    " where (julianday(A.ReportDay)-julianday('%s')) >=0 and (julianday('%s')-julianday(A.ReportDay)) >=0 order by A.ReportDay desc",fday,tDay);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            if (cursor.moveToFirst()) {
                do {
                    SM_ReportSaleRep oRptSaleRep = new SM_ReportSaleRep();
                    oRptSaleRep.setReportSaleId(cursor.getString(cursor.getColumnIndex("ReportSaleID")));
                    oRptSaleRep.setReportCode(cursor.getString(cursor.getColumnIndex("ReportCode")));
                    oRptSaleRep.setReportName(cursor.getString(cursor.getColumnIndex("ReportName")));
                    oRptSaleRep.setReportDay(cursor.getString(cursor.getColumnIndex("ReportDay")));
                    oRptSaleRep.setLongtitude(cursor.getFloat(cursor.getColumnIndex("Longitude")));
                    oRptSaleRep.setLatitude(cursor.getFloat(cursor.getColumnIndex("Latitude")));
                    oRptSaleRep.setLocationAddress(cursor.getString(cursor.getColumnIndex("LocationAddress")));
                    oRptSaleRep.setReceiverList(cursor.getString(cursor.getColumnIndex("ReceiverList")));
                    oRptSaleRep.setNotes(cursor.getString(cursor.getColumnIndex("Notes")));
                    String isStatus = cursor.getString(cursor.getColumnIndex("IsStatus"));
                    if(isStatus != null)
                    {
                        if(isStatus.equalsIgnoreCase("0"))
                        {
                            oRptSaleRep.setIsStatus("Phiếu mới");
                        }
                        else if(isStatus.equalsIgnoreCase("1"))
                        {
                            oRptSaleRep.setIsStatus("Đã điều chỉnh");
                        }
                        else if(isStatus.equalsIgnoreCase("3"))
                        {
                            oRptSaleRep.setIsStatus("Đã hủy");
                        }
                        else
                        {
                            oRptSaleRep.setIsStatus("");
                        }

                    }
                    String isPost = cursor.getString(cursor.getColumnIndex("IsPost"));
                    if(isPost.equalsIgnoreCase("1"))
                    {
                        oRptSaleRep.setPost(true);
                    }else{
                        oRptSaleRep.setPost(false);
                    }
                    oRptSaleRep.setPostDay(cursor.getString(cursor.getColumnIndex("PostDay")));

                    lst.add(oRptSaleRep);

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return lst;
        }catch (Exception ex){Log.d("ERR_LOAD_SALE_REP",ex.getMessage().toString());}
        return null;
    }

    public SM_ReportSaleRep getReportSaleRepById(String reportSaleId)
    {
        try {
            String mSql=String.format("Select A.* from SM_REPORT_SALEREP A "+
                    " where A.ReportSaleID='%s' order by ReportDay desc",reportSaleId);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            SM_ReportSaleRep oRptSaleRep = new SM_ReportSaleRep();
            if (cursor.moveToFirst()) {
                do {
                    oRptSaleRep.setReportSaleId(cursor.getString(cursor.getColumnIndex("ReportSaleID")));
                    oRptSaleRep.setReportCode(cursor.getString(cursor.getColumnIndex("ReportCode")));
                    oRptSaleRep.setReportName(cursor.getString(cursor.getColumnIndex("ReportName")));
                    oRptSaleRep.setReportDay(cursor.getString(cursor.getColumnIndex("ReportDay")));
                    oRptSaleRep.setLongtitude(cursor.getFloat(cursor.getColumnIndex("Longitude")));
                    oRptSaleRep.setLatitude(cursor.getFloat(cursor.getColumnIndex("Latitude")));
                    oRptSaleRep.setLocationAddress(cursor.getString(cursor.getColumnIndex("LocationAddress")));
                    oRptSaleRep.setReceiverList(cursor.getString(cursor.getColumnIndex("ReceiverList")));
                    oRptSaleRep.setNotes(cursor.getString(cursor.getColumnIndex("Notes")));
                    String isStatus = cursor.getString(cursor.getColumnIndex("IsStatus"));
                    if(isStatus != null)
                    {
                        if(isStatus.equalsIgnoreCase("0"))
                        {
                            oRptSaleRep.setIsStatus("Phiếu mới");
                        }
                        else if(isStatus.equalsIgnoreCase("1"))
                        {
                            oRptSaleRep.setIsStatus("Đã điều chỉnh");
                        }
                        else if(isStatus.equalsIgnoreCase("3"))
                        {
                            oRptSaleRep.setIsStatus("Đã hủy");
                        }
                        else
                        {
                            oRptSaleRep.setIsStatus("");
                        }

                    }
                    String isPost = cursor.getString(cursor.getColumnIndex("IsPost"));
                    if(isPost.equalsIgnoreCase("1"))
                    {
                        oRptSaleRep.setPost(true);
                    }else{
                        oRptSaleRep.setPost(false);
                    }
                    oRptSaleRep.setPostDay(cursor.getString(cursor.getColumnIndex("PostDay")));

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return oRptSaleRep;
        }catch (Exception ex){
            Log.d("ERR_LOAD_SALE_REP",ex.getMessage().toString());
        }
        return null;
    }

    public boolean editReportSale(SM_ReportSaleRep oRptSaleRep){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("ReportCode", oRptSaleRep.getReportCode());
            values.put("ReportName", oRptSaleRep.getReportName());

            values.put("ReportDay", oRptSaleRep.getReportDay());
            values.put("Longitude", oRptSaleRep.getLongtitude());
            values.put("Latitude", oRptSaleRep.getLatitude());
            values.put("LocationAddress", oRptSaleRep.getLocationAddress());
            values.put("ReceiverList", oRptSaleRep.getReceiverList());
            values.put("Notes", oRptSaleRep.getNotes());

            values.put("IsStatus", oRptSaleRep.getIsStatus());
            values.put("IsPost", oRptSaleRep.getPost());
            values.put("PostDay", oRptSaleRep.getPostDay());
            db.update("SM_REPORT_SALEREP",values,"ReportSaleID=?" ,new String[] {String.valueOf(oRptSaleRep.getReportSaleId())});
            db.close();
            return true;
        }catch (Exception e){Log.v("UDP_SM_RPT_SALE_ERR",e.getMessage()); return  false;}
    }

    public String addReportSaleRep(SM_ReportSaleRep oRptSaleRep){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int iSq = 1;

            String ReportID=getReportSaleRepId(oRptSaleRep.getReportDay());
            if(ReportID!=""){
                if(oRptSaleRep.getReportSaleId().isEmpty()|| oRptSaleRep.getReportSaleId()==null){
                    oRptSaleRep.setReportSaleId(ReportID);
                }
            }
            iSq=getSizeReportSaleRep(oRptSaleRep.getReportSaleId());
            if (iSq<=0) {
                ContentValues values = new ContentValues();
                values.put("ReportSaleID", oRptSaleRep.getReportSaleId());
                values.put("ReportCode", oRptSaleRep.getReportCode());
                values.put("ReportName", oRptSaleRep.getReportName());

                values.put("ReportDay", oRptSaleRep.getReportDay());
                values.put("Longitude", oRptSaleRep.getLongtitude());
                values.put("Latitude", oRptSaleRep.getLatitude());
                values.put("LocationAddress", oRptSaleRep.getLocationAddress());
                values.put("ReceiverList", oRptSaleRep.getReceiverList());
                values.put("Notes", oRptSaleRep.getNotes());

                values.put("IsStatus", oRptSaleRep.getIsStatus());
                values.put("IsPost", oRptSaleRep.getPost());
                values.put("PostDay", oRptSaleRep.getPostDay());

                db.insert("SM_REPORT_SALEREP", null, values);
            }else{
                ContentValues values = new ContentValues();
                values.put("ReportCode", oRptSaleRep.getReportCode());
                values.put("ReportName", oRptSaleRep.getReportName());

                values.put("ReportDay", oRptSaleRep.getReportDay());
                values.put("Longitude", oRptSaleRep.getLongtitude());
                values.put("Latitude", oRptSaleRep.getLatitude());
                values.put("LocationAddress", oRptSaleRep.getLocationAddress());
                values.put("ReceiverList", oRptSaleRep.getReceiverList());
                values.put("Notes", oRptSaleRep.getNotes());

                values.put("IsStatus", oRptSaleRep.getIsStatus());
                values.put("IsPost", oRptSaleRep.getPost());
                values.put("PostDay", oRptSaleRep.getPostDay());
                db.update("SM_REPORT_SALEREP",values,"ReportSaleID=?" ,new String[] {String.valueOf(oRptSaleRep.getReportSaleId())});
            }
            db.close();
            return "";
        }catch (Exception e){
            return  "ERR:"+e.getMessage();
        }
    }
    /*END REPORT SALE REP*/

    /* REPORT SALE REP THỊ TRƯỜNG */
    private String getReportSaleRepMarketID(String ReportSaleID){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT A.* FROM SM_REPORT_SALEREP_MARKET A LEFT JOIN SM_REPORT_SALEREP B ON A.ReportSaleID = B.ReportSaleID WHERE B.ReportSaleID=? ", new String[]{ReportSaleID});
            String MarketID="";
            if (cursor.moveToFirst()) {
                do {
                    MarketID=cursor.getString(cursor.getColumnIndex("MarketID"));
                    break;
                } while (cursor.moveToNext());
            }
            cursor.close();
            return MarketID;
        }catch(Exception ex){return  "";}
    }

    public int getSizeReportSaleRepMarket(String MarketID){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM SM_REPORT_SALEREP_MARKET WHERE MarketID=?", new String[]{MarketID});
            int iSq= cursor.getCount();
            cursor.close();
            return  iSq;
        }catch(Exception ex){return -1;}
    }

    public void CleanReportSaleRepMarket(int iIntervalDay) {
        try {
            SQLiteDatabase db = getWritableDatabase();

            String mSql = String.format("delete from SM_REPORT_SALEREP_MARKET where ReportSaleID in(select ReportSaleID from SM_REPORT_SALEREP where julianday('now')-julianday(ReportDay)>%s)", iIntervalDay);
            db.execSQL(mSql);

        } catch (Exception ex) {
        }
    }

    public List<SM_ReportSaleRepMarket> getAllReportSaleRepMarket(String mReportSaleId) {
        try {
            List<SM_ReportSaleRepMarket> lst = new ArrayList<SM_ReportSaleRepMarket>();
            String mSql=String.format("Select A.* from SM_REPORT_SALEREP_MARKET A LEFT JOIN SM_REPORT_SALEREP B ON A.ReportSaleID = B.ReportSaleID"+
                    " where A.ReportSaleID='%s' order by B.ReportDay desc", mReportSaleId);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            if (cursor.moveToFirst()) {
                do {
                    SM_ReportSaleRepMarket oRptSaleMarket = new SM_ReportSaleRepMarket();
                    oRptSaleMarket.setMarketId(cursor.getString(cursor.getColumnIndex("MarketID")));
                    oRptSaleMarket.setReportSaleId(cursor.getString(cursor.getColumnIndex("ReportSaleID")));
                    oRptSaleMarket.setCustomerId(cursor.getString(cursor.getColumnIndex("CustomerID")));
                    oRptSaleMarket.setCompanyName(cursor.getString(cursor.getColumnIndex("CompanyName")));
                    oRptSaleMarket.setProductCode(cursor.getString(cursor.getColumnIndex("ProductCode")));
                    oRptSaleMarket.setNotes(cursor.getString(cursor.getColumnIndex("Notes")));
                    oRptSaleMarket.setPrice(cursor.getFloat(cursor.getColumnIndex("Price")));
                    lst.add(oRptSaleMarket);

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return lst;
        }catch (Exception ex){Log.d("ERR_LOAD_SALE_MARKET",ex.getMessage().toString());}
        return null;
    }

    public SM_ReportSaleRepMarket getReportSaleRepMarketById(String MarketID)
    {
        try {
            String mSql=String.format("Select A.* from SM_REPORT_SALEREP_MARKET A LEFT JOIN SM_REPORT_SALEREP B ON A.ReportSaleID = B.ReportSaleID "+
                    " where A.MarketID='%s' order by B.ReportDay desc",MarketID);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            SM_ReportSaleRepMarket oRptSalehMarket = new SM_ReportSaleRepMarket();
            if (cursor.moveToFirst()) {
                do {
                    oRptSalehMarket.setMarketId(cursor.getString(cursor.getColumnIndex("MarketID")));
                    oRptSalehMarket.setReportSaleId(cursor.getString(cursor.getColumnIndex("ReportSaleID")));
                    oRptSalehMarket.setCustomerId(cursor.getString(cursor.getColumnIndex("CustomerID")));
                    oRptSalehMarket.setCompanyName(cursor.getString(cursor.getColumnIndex("CompanyName")));
                    oRptSalehMarket.setProductCode(cursor.getString(cursor.getColumnIndex("ProductCode")));
                    oRptSalehMarket.setNotes(cursor.getString(cursor.getColumnIndex("Notes")));
                    oRptSalehMarket.setPrice(cursor.getFloat(cursor.getColumnIndex("Price")));

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return oRptSalehMarket;
        }catch (Exception ex){
            Log.d("ERR_LOAD_SALE_MARKET",ex.getMessage().toString());
        }
        return null;
    }

    public String addReportSaleRepMarket(SM_ReportSaleRepMarket oRptSaleMarket){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int iSq = 1;

            String MarketID=getReportSaleRepMarketID(oRptSaleMarket.getReportSaleId());
            if(MarketID!=""){
                if(oRptSaleMarket.getMarketId().isEmpty()|| oRptSaleMarket.getMarketId()==null){
                    oRptSaleMarket.setMarketId(MarketID);
                }
            }
            iSq=getSizeReportSaleRepMarket(oRptSaleMarket.getMarketId());
            if (iSq<=0) {
                ContentValues values = new ContentValues();
                values.put("MarketID", oRptSaleMarket.getMarketId());
                values.put("ReportSaleID", oRptSaleMarket.getReportSaleId());
                values.put("CustomerID", oRptSaleMarket.getCustomerId());
                values.put("CompanyName", oRptSaleMarket.getCompanyName());
                values.put("ProductCode", oRptSaleMarket.getProductCode());
                values.put("Notes", oRptSaleMarket.getNotes());
                values.put("Price", oRptSaleMarket.getPrice());
                db.insert("SM_REPORT_SALEREP_MARKET", null, values);
            }else{
                ContentValues values = new ContentValues();
                values.put("ReportSaleID", oRptSaleMarket.getReportSaleId());
                values.put("CustomerID", oRptSaleMarket.getCustomerId());
                values.put("CompanyName", oRptSaleMarket.getCompanyName());
                values.put("ProductCode", oRptSaleMarket.getProductCode());
                values.put("Notes", oRptSaleMarket.getNotes());
                values.put("Price", oRptSaleMarket.getPrice());
                db.update("SM_REPORT_SALEREP_MARKET",values,"MarketID=?" ,new String[] {String.valueOf(oRptSaleMarket.getMarketId())});
            }
            db.close();
            return "";
        }catch (Exception e){
            return  "ERR:"+e.getMessage();
        }
    }

    /* REPORT SALE REP MÙA VỤ */
    private String getReportSaleRepSeasonID(String ReportSaleID){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT A.* FROM SM_REPORT_SALEREP_SEASON A LEFT JOIN SM_REPORT_SALEREP B ON A.ReportSaleID = B.ReportSaleID WHERE B.ReportSaleID=? ", new String[]{ReportSaleID});
            String DiseaseID="";
            if (cursor.moveToFirst()) {
                do {
                    DiseaseID=cursor.getString(cursor.getColumnIndex("SeasonID"));
                    break;
                } while (cursor.moveToNext());
            }
            cursor.close();
            return DiseaseID;
        }catch(Exception ex){return  "";}
    }
    public int getSizeReportSaleRepSeason(String SeasonID){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM SM_REPORT_SALEREP_SEASON WHERE SeasonID=?", new String[]{SeasonID});
            int iSq= cursor.getCount();
            cursor.close();
            return  iSq;
        }catch(Exception ex){return -1;}
    }

    public void CleanReportSaleRepSeason(int iIntervalDay) {
        try {
            SQLiteDatabase db = getWritableDatabase();

            String mSql = String.format("delete from SM_REPORT_SALEREP_SEASON where ReportSaleID in(select ReportSaleID from SM_REPORT_SALEREP where julianday('now')-julianday(ReportDay)>%s)", iIntervalDay);
            db.execSQL(mSql);

        } catch (Exception ex) {
        }
    }
    public List<SM_ReportSaleRepSeason> getAllReportSaleRepSeason(String ReportSaleID) {
        try {
            List<SM_ReportSaleRepSeason> lst = new ArrayList<SM_ReportSaleRepSeason>();
            String mSql=String.format("Select A.* from SM_REPORT_SALEREP_SEASON A LEFT JOIN SM_REPORT_SALEREP B ON A.ReportSaleID = B.ReportSaleID"+
                    " where A.ReportSaleID='%s' order by B.ReportDay desc", ReportSaleID);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            if (cursor.moveToFirst()) {
                do {
                    SM_ReportSaleRepSeason oRptSaleRepSeason = new SM_ReportSaleRepSeason();
                    oRptSaleRepSeason.setSeasonId(cursor.getString(cursor.getColumnIndex("SeasonID")));
                    oRptSaleRepSeason.setReportSaleId(cursor.getString(cursor.getColumnIndex("ReportSaleID")));
                    oRptSaleRepSeason.setTreeCode(cursor.getString(cursor.getColumnIndex("TreeCode")));
                    oRptSaleRepSeason.setSeasonCode(cursor.getString(cursor.getColumnIndex("SeasonCode")));
                    oRptSaleRepSeason.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                    oRptSaleRepSeason.setAcreage(cursor.getFloat(cursor.getColumnIndex("Acreage")));
                    oRptSaleRepSeason.setNotes(cursor.getString(cursor.getColumnIndex("Notes")));
                    lst.add(oRptSaleRepSeason);

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return lst;
        }catch (Exception ex){Log.d("ERR_LOAD_SALE_SEASON",ex.getMessage().toString());}
        return null;
    }

    public SM_ReportSaleRepSeason getReportSaleRepSeasonById(String SeasonID)
    {
        try {
            String mSql=String.format("Select A.* from SM_REPORT_SALEREP_SEASON A LEFT JOIN SM_REPORT_SALEREP B ON A.ReportSaleID = B.ReportSaleID "+
                    " where A.SeasonID='%s' order by B.ReportDay desc",SeasonID);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            SM_ReportSaleRepSeason oRptSaleRepSeason = new SM_ReportSaleRepSeason();
            if (cursor.moveToFirst()) {
                do {
                    oRptSaleRepSeason.setSeasonId(cursor.getString(cursor.getColumnIndex("SeasonID")));
                    oRptSaleRepSeason.setReportSaleId(cursor.getString(cursor.getColumnIndex("ReportSaleID")));
                    oRptSaleRepSeason.setTreeCode(cursor.getString(cursor.getColumnIndex("TreeCode")));
                    oRptSaleRepSeason.setSeasonCode(cursor.getString(cursor.getColumnIndex("SeasonCode")));
                    oRptSaleRepSeason.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                    oRptSaleRepSeason.setAcreage(cursor.getFloat(cursor.getColumnIndex("Acreage")));
                    oRptSaleRepSeason.setNotes(cursor.getString(cursor.getColumnIndex("Notes")));

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return oRptSaleRepSeason;
        }catch (Exception ex){
            Log.d("ERR_LOAD_SALE_SEASON",ex.getMessage().toString());
        }
        return null;
    }

    public String addReportSaleRepSeason(SM_ReportSaleRepSeason oRptTechSeason){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int iSq = 1;

            String seasonID=getReportSaleRepSeasonID(oRptTechSeason.getReportSaleId());
            if(seasonID!=""){
                if(oRptTechSeason.getSeasonId().isEmpty()|| oRptTechSeason.getSeasonId()==null){
                    oRptTechSeason.setSeasonId(seasonID);
                }
            }
            iSq=getSizeReportSaleRepSeason(oRptTechSeason.getSeasonId());
            if (iSq<=0) {
                ContentValues values = new ContentValues();
                values.put("SeasonID", oRptTechSeason.getSeasonId());
                values.put("ReportSaleID", oRptTechSeason.getReportSaleId());
                values.put("TreeCode", oRptTechSeason.getTreeCode());
                values.put("SeasonCode", oRptTechSeason.getSeasonCode());
                values.put("Title", oRptTechSeason.getTitle());
                values.put("Acreage", oRptTechSeason.getAcreage());
                values.put("Notes", oRptTechSeason.getNotes());
                db.insert("SM_REPORT_SALEREP_SEASON", null, values);
            }else{
                ContentValues values = new ContentValues();
                values.put("ReportSaleID", oRptTechSeason.getReportSaleId());
                values.put("TreeCode", oRptTechSeason.getTreeCode());
                values.put("SeasonCode", oRptTechSeason.getSeasonCode());
                values.put("Title", oRptTechSeason.getTitle());
                values.put("Acreage", oRptTechSeason.getAcreage());
                values.put("Notes", oRptTechSeason.getNotes());
                db.update("SM_REPORT_SALEREP_SEASON",values,"SeasonID=?" ,new String[] {String.valueOf(oRptTechSeason.getSeasonId())});
            }
            db.close();
            return "";
        }catch (Exception e){
            return  "ERR:"+e.getMessage();
        }
    }

    /* END REPORT SALE REP SEASON */

    /* REPORT SALE REP DỊCH BỆNH */
    private String getReportSaleRepDiseaseID(String ReportSaleID){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT A.* FROM SM_REPORT_SALEREP_DISEASE A LEFT JOIN SM_REPORT_SALEREP B ON A.ReportSaleID = B.ReportSaleID WHERE B.ReportSaleID=? ", new String[]{ReportSaleID});
            String DiseaseID="";
            if (cursor.moveToFirst()) {
                do {
                    DiseaseID=cursor.getString(cursor.getColumnIndex("DiseaseID"));
                    break;
                } while (cursor.moveToNext());
            }
            cursor.close();
            return DiseaseID;
        }catch(Exception ex){return  "";}
    }
    public int getSizeReportSaleRepDisease(String DiseaseID){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM SM_REPORT_SALEREP_DISEASE WHERE DiseaseID=?", new String[]{DiseaseID});
            int iSq= cursor.getCount();
            cursor.close();
            return  iSq;
        }catch(Exception ex){return -1;}
    }

    public void CleanReportSaleRepDisease(int iIntervalDay) {
        try {
            SQLiteDatabase db = getWritableDatabase();

            String mSql = String.format("delete from SM_REPORT_SALEREP_DISEASE where ReportSaleID in(select ReportSaleID from SM_REPORT_SALEREP where julianday('now')-julianday(ReportDay)>%s)", iIntervalDay);
            db.execSQL(mSql);

        } catch (Exception ex) {
        }
    }
    public List<SM_ReportSaleRepDisease> getAllReportSaleRepDisease(String ReportSaleID) {
        try {
            List<SM_ReportSaleRepDisease> lst = new ArrayList<SM_ReportSaleRepDisease>();
            String mSql=String.format("Select A.* from SM_REPORT_SALEREP_DISEASE A LEFT JOIN SM_REPORT_SALEREP B ON A.ReportSaleID = B.ReportSaleID"+
                    " where A.ReportSaleID='%s' order by B.ReportDay desc", ReportSaleID);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            if (cursor.moveToFirst()) {
                do {
                    SM_ReportSaleRepDisease oRptSaleRepDisease = new SM_ReportSaleRepDisease();
                    oRptSaleRepDisease.setDiseaseId(cursor.getString(cursor.getColumnIndex("DiseaseID")));
                    oRptSaleRepDisease.setReportSaleId(cursor.getString(cursor.getColumnIndex("ReportSaleID")));
                    oRptSaleRepDisease.setTreeCode(cursor.getString(cursor.getColumnIndex("TreeCode")));
                    oRptSaleRepDisease.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                    oRptSaleRepDisease.setArceage(cursor.getFloat(cursor.getColumnIndex("Acreage")));
                    oRptSaleRepDisease.setNotes(cursor.getString(cursor.getColumnIndex("Notes")));
                    lst.add(oRptSaleRepDisease);

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return lst;
        }catch (Exception ex){Log.d("ERR_LOAD_SALE_DISEASE",ex.getMessage().toString());}
        return null;
    }

    public SM_ReportSaleRepDisease getReportSaleRepDiseaseById(String DiseaseID)
    {
        try {
            String mSql=String.format("Select A.* from SM_REPORT_SALEREP_DISEASE A LEFT JOIN SM_REPORT_SALEREP B ON A.ReportSaleID = B.ReportSaleID "+
                    " where A.DiseaseID='%s' order by B.ReportDay desc",DiseaseID);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            SM_ReportSaleRepDisease oRptSaleRepDisease = new SM_ReportSaleRepDisease();
            if (cursor.moveToFirst()) {
                do {
                    oRptSaleRepDisease.setDiseaseId(cursor.getString(cursor.getColumnIndex("DiseaseID")));
                    oRptSaleRepDisease.setReportSaleId(cursor.getString(cursor.getColumnIndex("ReportSaleID")));
                    oRptSaleRepDisease.setTreeCode(cursor.getString(cursor.getColumnIndex("TreeCode")));
                    oRptSaleRepDisease.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                    oRptSaleRepDisease.setArceage(cursor.getFloat(cursor.getColumnIndex("Acreage")));
                    oRptSaleRepDisease.setNotes(cursor.getString(cursor.getColumnIndex("Notes")));

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return oRptSaleRepDisease;
        }catch (Exception ex){
            Log.d("ERR_LOAD_SALE_DISEASE",ex.getMessage().toString());
        }
        return null;
    }

    public String addReportSaleRepDisease(SM_ReportSaleRepDisease oRptTechDisease){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int iSq = 1;

            String DiseaseID=getReportSaleRepDiseaseID(oRptTechDisease.getReportSaleId());
            if(DiseaseID!=""){
                if(oRptTechDisease.getDiseaseId().isEmpty()|| oRptTechDisease.getDiseaseId()==null){
                    oRptTechDisease.setDiseaseId(DiseaseID);
                }
            }
            iSq=getSizeReportSaleRepDisease(oRptTechDisease.getDiseaseId());
            if (iSq<=0) {
                ContentValues values = new ContentValues();
                values.put("DiseaseID", oRptTechDisease.getDiseaseId());
                values.put("ReportSaleID", oRptTechDisease.getReportSaleId());
                values.put("TreeCode", oRptTechDisease.getTreeCode());
                values.put("Title", oRptTechDisease.getTitle());
                values.put("Acreage", oRptTechDisease.getArceage());
                values.put("Notes", oRptTechDisease.getNotes());
                db.insert("SM_REPORT_SALEREP_DISEASE", null, values);
            }else{
                ContentValues values = new ContentValues();
                values.put("ReportSaleID", oRptTechDisease.getReportSaleId());
                values.put("TreeCode", oRptTechDisease.getTreeCode());
                values.put("Title", oRptTechDisease.getTitle());
                values.put("Acreage", oRptTechDisease.getArceage());
                values.put("Notes", oRptTechDisease.getNotes());
                db.update("SM_REPORT_SALEREP_DISEASE",values,"DiseaseID=?" ,new String[] {String.valueOf(oRptTechDisease.getDiseaseId())});
            }
            db.close();
            return "";
        }catch (Exception e){
            return  "ERR:"+e.getMessage();
        }
    }

    /* END REPORT SALE REP DICH BENH */

    /* REPORT SALE REP HOAT DONG */
    private String getReportSaleRepActivityID(String ReportSaleID){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT A.* FROM SM_REPORT_SALEREP_ACTIVITIE A LEFT JOIN SM_REPORT_SALEREP B ON A.ReportSaleID = B.ReportSaleID WHERE B.ReportSaleID=? ", new String[]{ReportSaleID});
            String DiseaseID="";
            if (cursor.moveToFirst()) {
                do {
                    DiseaseID=cursor.getString(cursor.getColumnIndex("ActivitieID"));
                    break;
                } while (cursor.moveToNext());
            }
            cursor.close();
            return DiseaseID;
        }catch(Exception ex){return  "";}
    }
    public int getSizeReportSaleRepActivity(String ActivitieID){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM SM_REPORT_SALEREP_ACTIVITIE WHERE ActivitieID=?", new String[]{ActivitieID});
            int iSq= cursor.getCount();
            cursor.close();
            return  iSq;
        }catch(Exception ex){return -1;}
    }

    public void CleanReportSaleRepActivity(int iIntervalDay) {
        try {
            SQLiteDatabase db = getWritableDatabase();

            String mSql = String.format("delete from SM_REPORT_SALEREP_ACTIVITIE where ReportSaleID in(select ReportSaleID from SM_REPORT_SALEREP where julianday('now')-julianday(ReportDay)>%s)", iIntervalDay);
            db.execSQL(mSql);

        } catch (Exception ex) {
        }
    }
    public List<SM_ReportSaleRepActivitie> getAllReportSaleRepAcitivity(String ActivitieID) {
        try {
            List<SM_ReportSaleRepActivitie> lst = new ArrayList<SM_ReportSaleRepActivitie>();
            String mSql=String.format("Select A.* from SM_REPORT_SALEREP_ACTIVITIE A LEFT JOIN SM_REPORT_SALEREP B ON A.ReportSaleID = B.ReportSaleID"+
                    " where A.ActivitieID='%s' order by B.ReportDay desc", ActivitieID);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            if (cursor.moveToFirst()) {
                do {
                    SM_ReportSaleRepActivitie oRpt = new SM_ReportSaleRepActivitie();
                    oRpt.setActivitieId(cursor.getString(cursor.getColumnIndex("ActivitieID")));
                    oRpt.setReportSaleId(cursor.getString(cursor.getColumnIndex("ReportSaleID")));
                    oRpt.setIsType(cursor.getString(cursor.getColumnIndex("IsType")));
                    oRpt.setWorkDay(cursor.getString(cursor.getColumnIndex("Workday")));
                    oRpt.setPlace(cursor.getString(cursor.getColumnIndex("Place")));
                    oRpt.setNotes(cursor.getString(cursor.getColumnIndex("Notes")));
                    oRpt.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                    lst.add(oRpt);

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return lst;
        }catch (Exception ex){Log.d("ERR_LOAD_SALE_ACTIVITY",ex.getMessage().toString());}
        return null;
    }

    public SM_ReportSaleRepActivitie getReportSaleRepActivityById(String ActivitieID)
    {
        try {
            String mSql=String.format("Select A.* from SM_REPORT_SALEREP_ACTIVITIE A LEFT JOIN SM_REPORT_SALEREP B ON A.ReportSaleID = B.ReportSaleID "+
                    " where A.ActivitieID='%s' order by B.ReportDay desc",ActivitieID);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            SM_ReportSaleRepActivitie oRpt = new SM_ReportSaleRepActivitie();
            if (cursor.moveToFirst()) {
                do {
                    oRpt.setActivitieId(cursor.getString(cursor.getColumnIndex("ActivitieID")));
                    oRpt.setReportSaleId(cursor.getString(cursor.getColumnIndex("ReportSaleID")));
                    oRpt.setIsType(cursor.getString(cursor.getColumnIndex("IsType")));
                    oRpt.setWorkDay(cursor.getString(cursor.getColumnIndex("Workday")));
                    oRpt.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                    oRpt.setPlace(cursor.getString(cursor.getColumnIndex("Place")));
                    oRpt.setNotes(cursor.getString(cursor.getColumnIndex("Notes")));

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return oRpt;
        }catch (Exception ex){
            Log.d("ERR_LOAD_SALE_DISEASE",ex.getMessage().toString());
        }
        return null;
    }

    public String addReportSaleRepActivity(SM_ReportSaleRepActivitie oRpt){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int iSq = 1;

            String DiseaseID=getReportSaleRepActivityID(oRpt.getReportSaleId());
            if(DiseaseID!=""){
                if(oRpt.getActivitieId().isEmpty()|| oRpt.getActivitieId()==null){
                    oRpt.setActivitieId(DiseaseID);
                }
            }
            iSq=getSizeReportSaleRepActivity(oRpt.getActivitieId());
            if (iSq<=0) {
                ContentValues values = new ContentValues();
                values.put("ActivitieID", oRpt.getActivitieId());
                values.put("ReportSaleID", oRpt.getReportSaleId());
                values.put("IsType", oRpt.getIsType());
                values.put("Workday", oRpt.getWorkDay());
                values.put("Title", oRpt.getTitle());
                values.put("Place", oRpt.getPlace());
                values.put("Notes", oRpt.getNotes());
                db.insert("SM_REPORT_SALEREP_ACTIVITIE", null, values);
            }else{
                ContentValues values = new ContentValues();
                values.put("ReportSaleID", oRpt.getReportSaleId());
                values.put("IsType", oRpt.getIsType());
                values.put("Workday", oRpt.getWorkDay());
                values.put("Title", oRpt.getTitle());
                values.put("Place", oRpt.getPlace());
                values.put("Notes", oRpt.getNotes());
                db.update("SM_REPORT_SALEREP_ACTIVITIE",values,"ActivitieID=?" ,new String[] {String.valueOf(oRpt.getActivitieId())});
            }
            db.close();
            return "";
        }catch (Exception e){
            return  "ERR:"+e.getMessage();
        }
    }

    public List<SM_ReportSaleRepActivitie> getAllReportSaleRepActivity(String mReportSaleId, Integer type) {
        try {
            List<SM_ReportSaleRepActivitie> lst = new ArrayList<SM_ReportSaleRepActivitie>();
            String mSql=String.format("Select A.* from SM_REPORT_SALEREP_ACTIVITIE A LEFT JOIN SM_REPORT_SALEREP B ON A.ReportSaleID = B.ReportSaleID"+
                    " where A.ReportSaleID='%s' and A.IsType=%d order by B.ReportDay desc", mReportSaleId, type);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            if (cursor.moveToFirst()) {
                do {
                    SM_ReportSaleRepActivitie oRptTechActivity = new SM_ReportSaleRepActivitie();
                    oRptTechActivity.setActivitieId(cursor.getString(cursor.getColumnIndex("ActivitieID")));
                    oRptTechActivity.setReportSaleId(cursor.getString(cursor.getColumnIndex("ReportSaleID")));
                    oRptTechActivity.setIsType(cursor.getString(cursor.getColumnIndex("IsType")));
                    oRptTechActivity.setWorkDay(cursor.getString(cursor.getColumnIndex("Workday")));
                    oRptTechActivity.setPlace(cursor.getString(cursor.getColumnIndex("Place")));
                    oRptTechActivity.setNotes(cursor.getString(cursor.getColumnIndex("Notes")));
                    oRptTechActivity.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                    lst.add(oRptTechActivity);

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return lst;
        }catch (Exception ex){Log.d("ERR_LOAD_SALE_ACTIVITY",ex.getMessage().toString());}
        return null;
    }

    /* END REPORT SALE REP HOAT DONG */

    public boolean delReportSale(String ReportSaleID){
        try {
            SQLiteDatabase db = getWritableDatabase();
            String mSqlReport=String.format("delete from SM_REPORT_SALEREP where ReportSaleID ='%s'",ReportSaleID);
            String mSqlMarket=String.format("delete from SM_REPORT_SALEREP_MARKET where ReportSaleID ='%s'",ReportSaleID);
            String mSqlDisease=String.format("delete from SM_REPORT_SALEREP_DISEASE where ReportSaleID ='%s'",ReportSaleID);
            String mSqlCompetitor=String.format("delete from SM_REPORT_SALEREP_SEASON where ReportSaleID ='%s'",ReportSaleID);
            String mSqlActivity=String.format("delete from SM_REPORT_SALEREP_ACTIVITIE where ReportSaleID ='%s'",ReportSaleID);

            try {
                db.execSQL(mSqlReport);
                db.execSQL(mSqlMarket);
                db.execSQL(mSqlDisease);
                db.execSQL(mSqlCompetitor);
                db.execSQL(mSqlActivity);

            }catch (Exception ex){
                Log.d("DEL_SM_RPTSALE",ex.getMessage());
                return  false;
            }
            return  true;
        }catch (Exception ex){return false;}
    }

    public boolean delReportSaleDetail(String ReportSaleID){
        try {
            SQLiteDatabase db = getWritableDatabase();
            String mSqlMarket=String.format("delete from SM_REPORT_SALEREP_MARKET where ReportSaleID ='%s'",ReportSaleID);
            String mSqlDisease=String.format("delete from SM_REPORT_SALEREP_DISEASE where ReportSaleID ='%s'",ReportSaleID);
            String mSqlCompetitor=String.format("delete from SM_REPORT_SALEREP_SEASON where ReportSaleID ='%s'",ReportSaleID);
            String mSqlActivity=String.format("delete from SM_REPORT_SALEREP_ACTIVITIE where ReportSaleID ='%s'",ReportSaleID);

            try {
                db.execSQL(mSqlMarket);
                db.execSQL(mSqlDisease);
                db.execSQL(mSqlCompetitor);
                db.execSQL(mSqlActivity);

            }catch (Exception ex){
                Log.d("DEL_SM_ODT",ex.getMessage());
                return  false;
            }
            return  true;
        }catch (Exception ex){return false;}
    }
    /* END REPORT SALE REP THI TRUONG */

    public List<DM_Season> getAllSeason(){
        try {
            List<DM_Season> lst = new ArrayList<DM_Season>();
            String mSql=String.format("select * from DM_SEASON");

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            if (cursor.moveToFirst()) {
                do {
                    DM_Season season = new DM_Season();
                    season.setSeasonCode(cursor.getString(cursor.getColumnIndex("SeasonCode")));
                    season.setSeasonName(cursor.getString(cursor.getColumnIndex("SeasonName")));

                    lst.add(season);

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return lst;
        }catch (Exception ex){Log.d("ERR_LOAD_SEASON",ex.getMessage().toString());}
        return null;
    }

    public DM_Season getSeasonByCode(String code){
        try {
            String mSql=String.format("select * from DM_SEASON where SeasonCode='%s'", code);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(mSql, null);
            DM_Season season = new DM_Season();
            if (cursor.moveToFirst()) {
                do {
                    season.setSeasonCode(cursor.getString(cursor.getColumnIndex("SeasonCode")));
                    season.setSeasonName(cursor.getString(cursor.getColumnIndex("SeasonName")));

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return season;
        }catch (Exception ex){Log.d("ERR_LOAD_SEASON",ex.getMessage().toString());}
        return null;
    }

    public boolean delSeason(){
        String mSqlMarket=String.format("delete from DM_SEASON ");
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            db.execSQL(mSqlMarket);
        }catch (Exception ex){
            Log.d("DEL_SM_SEASON",ex.getMessage());
            return  false;
        }
        return  true;
    }

    public int getSizeSeason(String code){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM DM_SEASON WHERE SeasonCode=?", new String[]{code});
            int iSq= cursor.getCount();
            cursor.close();
            return  iSq;
        }catch(Exception ex){return -1;}
    }

    public boolean addSeason(DM_Season obj){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int iSq = 1;

            iSq=getSizeSeason(obj.getSeasonCode());
            if (iSq<=0) {
                ContentValues values = new ContentValues();
                values.put("SeasonID","");
                values.put("SeasonCode", obj.getSeasonCode());
                values.put("SeasonName", obj.getSeasonName());
                db.insert("DM_SEASON", null, values);
            }else{
                iSq=iSq+1;
                ContentValues values = new ContentValues();
                values.put("SeasonID","");
                values.put("SeasonCode", obj.getSeasonCode());
                values.put("SeasonName", obj.getSeasonName());
                db.update("DM_SEASON",values,"SeasonCode=?" ,new String[] { String.valueOf(obj.getSeasonCode())});
            }
            db.close();

            return true;
        }catch (Exception e){Log.v("INS_DISTRICT_ERR",e.getMessage()); return  false;}
    }

    // KE HOACH BAN HANG

    //<<SYSTEM-FUNCTION>>
    public String fFormatNgay(String ngay, String sFormatFrom, String sFormatTo){
        if (ngay==null || ngay.contains("null") || ngay.equals("")) return  sFormatFrom=="yyyy-MM-dd"?"01/01/1900":"1900-01-01";
        String sCastNgay="";
        try{
            Date date = new SimpleDateFormat(sFormatFrom).parse(ngay);
            sCastNgay = new SimpleDateFormat(sFormatTo).format(date);
            return sCastNgay;
        }catch (Exception ex){return (sFormatFrom=="yyyy-MM-dd"?"01/01/1900":"1900-01-01");}
    }
    //<<END>>

}
