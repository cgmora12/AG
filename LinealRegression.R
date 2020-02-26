library(readxl)
datos <- read_excel("ValidationResults.xlsx")

summary(datos)

#Rows (nº)         Columns (nº)     Cells (nº)        Generation time (s)
#Min.   :      13   Min.   : 3.00   Min.   :       39   Min.   : 9.70      
#Median :    2133   Median :13.50   Median :    31492   Median :10.20      
#Mean   : 2060744   Mean   :17.15   Mean   : 44658992   Mean   :15.92      
#1st Qu.:     154   1st Qu.: 6.50   1st Qu.:     1668   1st Qu.: 9.70      
#3rd Qu.:   81834   3rd Qu.:17.25   3rd Qu.:  1350056   3rd Qu.:10.90      
#Max.   :21072480   Max.   :57.00   Max.   :379304640   Max.   :80.00      
#NA's   :1          NA's   :1       NA's   :1           

#Linear model 
# y = Time
# x = Records
recta<-lm( datos$`Generation time (s)` ~datos$`Rows (nº)`)

summary(recta)

#Call:
#  lm(formula = datos$`Generation time (s)` ~ datos$`Rows (nº)`)
#Residuals:
#  Min      1Q  Median      3Q     Max 
#-7.3103  0.3390  0.5390  0.8315  3.5651 #

#Coefficients:
#  Estimate Std. Error t value Pr(>|t|)    
#  (Intercept)       9.361e+00  5.540e-01    16.9 1.73e-12 ***
#  datos$`Rows (nº)` 3.183e-06  1.020e-07    31.2  < 2e-16 ***
#  ---
#  Signif. codes:  0 ‘***’ 0.001 ‘**’ 0.01 ‘*’ 0.05 ‘.’ 0.1 ‘ ’ 1

#Residual standard error: 2.292 on 18 degrees of freedom
#(1 observation deleted due to missingness)
#Multiple R-squared:  0.9818,	Adjusted R-squared:  0.9808 
#F-statistic: 973.7 on 1 and 18 DF,  p-value: < 2.2e-16


