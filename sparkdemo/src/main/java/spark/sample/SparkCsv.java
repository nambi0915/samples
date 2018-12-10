package spark.sample;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class SparkCsv {
	public static void main(String[] args) throws AnalysisException {
		// System.out.println("Hello World!");

		System.setProperty("hadoop.home.dir", "C:\\Users\\Nambi\\Downloads\\");
		SparkConf conf = new SparkConf().setAppName("firstSparkProject").setMaster("local[*]");
		SparkContext sparkContext = new SparkContext(conf);
		sparkContext.setLogLevel("ERROR");

		SparkSession spark = SparkSession.builder().appName("firstSparkProject").config(conf).getOrCreate();
		// JavaSparkContext sc = new JavaSparkContext(conf);
		// String path = "linescount.txt";
		Dataset<Row> csv = spark.read().format("csv").option("header", "true")
				.load("C:\\Users\\Nambi\\Downloads\\Spark\\WA_Sales_Products_2012-14.csv");

		csv.show();
		csv.createOrReplaceTempView("productInfo");
		/*
		 * Dataset<Row> sql = spark.sql(
		 * "select Retailer_country,count(*) from productInfo where year=2013 and Product_type='Watches' group by Retailer_country order by 2 desc"
		 * );
		 */
		/*
		 * Dataset<Row> sql = spark.sql(
		 * "select Retailer_country,count(*) as sold from ProductInfo where year=2013	 and Product_type='Watches' group by Retailer_country order by sold desc limit 1"
		 * );
		 */

		Dataset<Row> moreWatchesIn2013 = spark.sql(
				"select Retailer_country,sum(Quantity) from productInfo where year=2013 and Product_type='Watches' group by Retailer_country order by 2 desc limit 1");
		moreWatchesIn2013.show();

		// spark.sql("select distinct order_method_type from productInfo").show();
		Dataset<Row> revEachYearEachType = spark.sql(
				"select year,order_method_type,sum(revenue) as revenue from productInfo group by year,order_method_type");
		revEachYearEachType.createOrReplaceTempView("all_type_revenue");

		Dataset<Row> maxRevenueEachYear = spark.sql(
				"select all_type_revenue.year,all_type_revenue.order_method_type,all_type_revenue.revenue from all_type_revenue inner join (select max(revenue) as maxrev from all_type_revenue group by year)max_revenue on all_type_revenue.revenue=max_revenue.maxrev");
		maxRevenueEachYear.show();

	}
}
