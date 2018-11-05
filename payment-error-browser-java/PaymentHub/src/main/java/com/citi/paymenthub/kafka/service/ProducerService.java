package com.citi.paymenthub.kafka.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.citi.paymenthub.kafka.config.KafkaProducerConfig;

/**
 * Producer Service
 * 
 * @author Yogesh Mohite
 * @CreationDate 26/10/2018
 * @version 1.0
 */
@Component
public class ProducerService {

	private Logger logger = LogManager.getLogger(ProducerService.class);

	public  List<Map<String,Object>> runProducer(final int sendMessageCount,String topic) throws Exception {
	      final Producer<Long, String> producer = KafkaProducerConfig.getProducer();
	      long time = System.currentTimeMillis();
	      List<Map<String,Object>> list= new ArrayList<Map<String,Object>>();
	      try {
	    	  int cnt=1;
	    	 // for(String topic:topicList) {
	          for (long index = time; index < time + sendMessageCount; index++) {
	        	  DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm:ss"); 
	              Date result = new Date(System.currentTimeMillis()); 
	              final ProducerRecord<Long, String> record =
	              Map<String,Object> map=new HashMap<String,Object>();
	              RecordMetadata metadata = producer.send(record).get();
	              map.put("Key", record.key());
	              //map.put("Value", record.value());
	              map.put("Offset", metadata.offset());
	              list.add(map);
	              /*System.out.printf("sent record(key=%s value=%s) " +
	                              "meta(partition=%d, offset=%d) time=%d\n",
	                      record.key(), record.value(), metadata.partition(),
	                      metadata.offset(), elapsedTime);*/
	              System.out.println("count==="+cnt++);
	          }
	    	//  }
	      } finally {
	          producer.flush();
	          producer.close();
	      }
	      return list;
	  }

	/**
	 * Method reprocess the error message which is come from Error message topic.
	 * @param topic
	 * @param errorMessage
	 */
	public void errorMessageReplayCall(String topic, String errorMessage) {
		final Producer<Long, String> producer = KafkaProducerConfig.getProducer();
		try {
			final ProducerRecord<Long, String> record = new ProducerRecord<>(topic, errorMessage);
			producer.send(record).get();

		} catch (Exception e) {
			logger.error(e);
		} finally {
			producer.flush();
			producer.close();
		}
	}

}