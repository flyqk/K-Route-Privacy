 setwd('C:\\Users\\Kan Qi\\OneDrive\\WorkSpace\\K-route-privacy\\data');
 routePrivacyData = read.csv('analysisResults.csv', header=TRUE, sep=",");
 boxplot(F~K, data=routePrivacyData, main="Frequencies allowed for (K,15min)-route-privacy around USC Campus", xlab="Required K routes over 15 minutes", ylab="Allowed frequency F (seconds)");