package com.linked_x2_in.demo.Repository;

import com.linked_x2_in.demo.DTOs.Course;
import com.linked_x2_in.demo.DTOs.Education;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CVRepository {

    private static Logger logger = LoggerFactory.getLogger(LinkedUserRepository.class);
    // Why This Failure marker
    private static final Marker WTF_MARKER = MarkerFactory.getMarker("WTF");

    public void createCV(String userURI){
        String strQuery =
                " PREFIX cv: "+ConfStrings.resume
                        +" INSERT DATA {"

                        +" <"+userURI+"/CV> "
                        +" cv:aboutPerson "
                        +" <"+userURI+">. "

                        +" }";

        execUpdate(strQuery);
    }

    public boolean checkIfExists(String userURI){

        String strQuery =
                " PREFIX cv: "+ConfStrings.resume
                        +" SELECT  ?cv "
                        +"WHERE { "

                        +" <"+userURI+"/CV> "
                        +" cv:aboutPerson "
                        +" ?cv. "

                        +" }";


        QueryExecution queryExecution = QueryExecutionFactory
                .sparqlService(ConfStrings.REPO_QUERY, strQuery);
        ResultSet resultSet = queryExecution.execSelect();

        return resultSet.hasNext();

    }

    public void delete(String userURI){
        String strQuery =
                " PREFIX cv: "+ConfStrings.resume
                        +" DELETE DATA {"

                        +" <"+userURI+"/CV> "
                        +" cv:aboutPerson "
                        +" <"+userURI+">. "

                        +" }";
        execUpdate(strQuery);
    }


    private static void execUpdate(String query){
        try{
            UpdateRequest updateRequest = UpdateFactory.create(query);
            UpdateProcessor updateProcessor = UpdateExecutionFactory
                    .createRemote(updateRequest,
                            ConfStrings.REPO_UPDATE);
            updateProcessor.execute();
        } catch (Throwable t) {
            logger.error(WTF_MARKER, t.getMessage(), t);
        }
    }

}
