CREATE OR REPLACE TYPE OBJECT_FUEL_RECIPE IS OBJECT (
  VOUCHER_TYP VARCHAR2(8), 
  VOUCHER_DTE DATE, 
  COA_CDE VARCHAR2(32), 
  QTY NUMBER, 
  AMNT NUMBER,
  ITEM_COA_CDE VARCHAR2(32),
  ITEM_QTY NUMBER, 
  ITEM_AMNT NUMBER, 
  ITEM_VALUE NUMBER 
);
/
CREATE OR REPLACE TYPE TABLE_FUEL_RECIPE IS TABLE OF OBJECT_FUEL_RECIPE;
/
CREATE OR REPLACE
FUNCTION GET_FUEL_RECIPE(FP_FROM_DTE IN VARCHAR2, FP_END_DTE IN VARCHAR2, FP_SITE_ID IN NUMBER, FP_COMPANY_ID IN NUMBER) 
RETURN TABLE_FUEL_RECIPE PIPELINED IS
CURSOR CS_RECIPE IS SELECT SRI.COA_CDE FROM SITE_RECIPE_ITEM SRI WHERE SRI.SITE_ID=FP_SITE_ID AND SRI.COMPANY_ID=FP_COMPANY_ID;
CURSOR CS_RECIPE_ITEM(CP_COA_CDE IN VARCHAR2) IS SELECT RD.COA_CDE, NVL(RD.QTY,0) QTY FROM RECIPE_DETAIL RD, RECIPE_MASTER RM 
                          WHERE RD.RECIPE_MASTER_ID=RM.RECIPE_MASTER_ID AND RM.SITE_ID=FP_SITE_ID AND RM.COMPANY_ID=FP_COMPANY_ID
                            AND RM.COA_CDE=CP_COA_CDE;
CURSOR CS_FUEL_RECIPE(CP_COA_CDE IN VARCHAR2, CP_ITEM_COA_CDE IN VARCHAR2, CP_RECIPE_ITEM_QTY IN NUMBER) IS ( 
    SELECT VST.VOUCHER_TYP, VM.VOUCHER_DTE, VD.COA_CDE, NVL(VD.QTY,0) QTY, NVL(VD.AMNT,0) AMNT, 
        ROUND((NVL(VD.QTY,0) * CP_RECIPE_ITEM_QTY),2) ITEM_QTY,
        ROUND((ROUND((NVL(VD.QTY,0) *  CP_RECIPE_ITEM_QTY),2) * ROUND((NVL(VD.AMNT,0)/NVL(VD.QTY,0)),4)),2) ITEM_AMNT,        
        ROUND((ROUND((NVL(VD.QTY,0) *  CP_RECIPE_ITEM_QTY),2) * (ALBERTA.GET_RECIPE_ITEM_COST_PRICE(TO_CHAR(VM.VOUCHER_DTE,'DD-MM-YYYY'),CP_ITEM_COA_CDE,VM.SITE_ID,VM.FIN_YEAR_ID,VM.COMPANY_ID))),2) ITEM_VALUE
        FROM VOUCHER_DETAIL VD, VOUCHER_MASTER VM, VOUCHER_SUB_TYPE VST, SITE S
        WHERE VD.VOUCHER_MASTER_ID = VM.VOUCHER_MASTER_ID AND VM.VOUCHER_SUB_TYP_ID = VST.VOUCHER_SUB_TYP_ID
        AND VM.CANCELLED_BY IS NULL AND VM.POSTED_IND='Y' AND VM.SITE_ID = FP_SITE_ID AND NVL(VD.QTY,0)<>0
        AND VM.SITE_ID = S.SITE_ID AND S.BUYSELL_START_DTE IS NOT NULL AND VM.VOUCHER_DTE >= S.BUYSELL_START_DTE
        AND VM.VOUCHER_DTE BETWEEN TO_DATE(FP_FROM_DTE,'DD-MM-YYYY') AND TO_DATE(FP_END_DTE,'DD-MM-YYYY')
        AND VD.COA_CDE=CP_COA_CDE AND VST.VOUCHER_TYP = 'SI' AND VM.COMPANY_ID = FP_COMPANY_ID );
BEGIN
  FOR FL_R IN CS_RECIPE LOOP
    FOR FL_RI IN CS_RECIPE_ITEM(FL_R.COA_CDE) LOOP
      FOR FL_FR IN CS_FUEL_RECIPE(FL_R.COA_CDE,FL_RI.COA_CDE,FL_RI.QTY) LOOP
        PIPE ROW (OBJECT_FUEL_RECIPE(FL_FR.VOUCHER_TYP, FL_FR.VOUCHER_DTE, FL_FR.COA_CDE, FL_FR.QTY, FL_FR.AMNT, FL_RI.COA_CDE, FL_FR.ITEM_QTY, FL_FR.ITEM_AMNT, FL_FR.ITEM_VALUE));
      END LOOP;  
    END LOOP;
  END LOOP;
  RETURN;  
END;
/