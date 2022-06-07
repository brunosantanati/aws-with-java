package com.dynamodb.repositories;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

//import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;

@Repository
public class MovieDynamoRepository {
	
	@Autowired
	private AmazonDynamoDB client;
	
	public void findByKey(String key) {
//		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
//				.withRegion(Regions.US_WEST_2).build();
				DynamoDB dynamoDB = new DynamoDB(client);

				Table table = dynamoDB.getTable("movie");

				QuerySpec spec = new QuerySpec()
				    .withKeyConditionExpression("FilmId = :v_id")
				    .withValueMap(new ValueMap()
				        .withString(":v_id", key));

				ItemCollection<QueryOutcome> items = table.query(spec);

				Iterator<Item> iterator = items.iterator();
				Item item = null;
				while (iterator.hasNext()) {
				    item = iterator.next();
				    System.out.println(item.toJSONPretty());
				}

	}

}
