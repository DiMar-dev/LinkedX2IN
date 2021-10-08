package com.linked_x2_in.demo.Repository;

import com.linked_x2_in.demo.DTOs.Course;
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
public class SkillsRepository {

    private static Logger logger = LoggerFactory.getLogger(LinkedUserRepository.class);
    // Why This Failure marker
    private static final Marker WTF_MARKER = MarkerFactory.getMarker("WTF");


    public void addSkill(String CV_URI, String skill){
        String strQuery =
                " PREFIX xsd: "+ConfStrings.xsd
                        +" PREFIX cv: "+ConfStrings.resume
                        +" INSERT DATA {"

                        +" <"+CV_URI+"> "
                        +" cv:hasSkill "
                        +" <"+CV_URI+"/Skill/"+skill+">. "


                        +" }";

        execUpdate(strQuery);
    }

    public List<String> findAllByCV(String CV_URI) {

        String strQuery =
                " PREFIX cv: "+ConfStrings.resume
                        +" SELECT ?skill "
                        +"WHERE { "

                        +" <"+CV_URI+"> "
                        +" cv:hasSkill "
                        +" ?skill."

                        +" }";

        QueryExecution queryExecution = QueryExecutionFactory
                .sparqlService(ConfStrings.REPO_QUERY, strQuery);
        ResultSet resultSet = queryExecution.execSelect();

        List<String> result = new ArrayList<>();
        while (resultSet.hasNext()){
            QuerySolution qs = resultSet.next();

            result.add(qs.get("skill").asResource().getLocalName());
        }

        queryExecution.close();
        return result;
    }

    public void remove(String CV_URI, String skill){
        String strQuery =
                " PREFIX cv: "+ConfStrings.resume
                        +" DELETE DATA { "

                        +" <"+CV_URI+"> "
                        +" cv:hasSkill "
                        +" <"+CV_URI+"/Skill/"+skill+">. "

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
