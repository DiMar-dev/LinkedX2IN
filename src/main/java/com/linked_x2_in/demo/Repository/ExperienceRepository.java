package com.linked_x2_in.demo.Repository;

import com.linked_x2_in.demo.DTOs.Education;
import com.linked_x2_in.demo.DTOs.Experience;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ExperienceRepository {

    private static Logger logger = LoggerFactory.getLogger(LinkedUserRepository.class);
    // Why This Failure marker
    private static final Marker WTF_MARKER = MarkerFactory.getMarker("WTF");


    public void addExperience(String CV_URI, Experience experience){
        String strQuery =
                " PREFIX xsd: "+ConfStrings.xsd
                        +" PREFIX cv: "+ConfStrings.resume
                        +" INSERT DATA {"

                        +" <"+CV_URI+"> "
                        +" cv:hasWorkHistory "
                        +" <"+CV_URI+"/WorkHistory/"+experience.getCompanyName()+">. "

                        +" <"+CV_URI+"/WorkHistory/"+experience.getCompanyName()+"> "
                        +" cv:employedIn "
                        +" <"+experience.getCompanyLink()+">; "

                        + " cv:jobTitle "
                        + "\""+ experience.getJobTitle() +"\"^^xsd:string;"

                        + " cv:startDate "
                        + "\""+ experience.getStartDate() +"\"^^xsd:date;"

                        + " cv:endDate "
                        + "\""+ experience.getEndDate() +"\"^^xsd:date."

                        +" }";

        execUpdate(strQuery);
    }


    public List<Experience> findAllByCV(String CV_URI){

        String strQuery =
                " PREFIX cv: "+ConfStrings.resume
                        +" SELECT  ?link ?title ?start ?end "
                        +"WHERE { "

                        +" <"+CV_URI+"> "
                        +" cv:hasWorkHistory "
                        +" ?uri."

                        +" ?uri"
                        +" cv:employedIn "
                        +" ?link;"

                        +" cv:jobTitle "
                        +" ?title;"

                        + " cv:startDate "
                        + " ?start;"

                        + " cv:endDate "
                        + " ?end."

                        +" }";


        QueryExecution queryExecution = QueryExecutionFactory
                                .sparqlService(ConfStrings.REPO_QUERY, strQuery);
        ResultSet resultSet = queryExecution.execSelect();

        List<Experience> result = new ArrayList<>();
        while (resultSet.hasNext()){
            QuerySolution qs = resultSet.next();

            result.add(Experience.builder()
                    .companyName(qs.get("link").asResource().getLocalName())
                    .companyLink(qs.get("link").asResource().getURI())
                    .jobTitle(qs.get("title").asLiteral().getString())
                    .startDate(qs.get("start").asLiteral().getString())
                    .endDate(qs.get("end").asLiteral().getString())
                    .build());
        }

        queryExecution.close();
        return result;

    }

    public void delete(String CV_URI, String expURI, Experience experience){

        String strQuery =
                " PREFIX cv: "+ConfStrings.resume
                        +"DELETE DATA { "

                        +" <"+CV_URI+"> "
                        +" cv:hasWorkHistory "
                        +" <"+expURI+">. "

                        +" <"+expURI+"> "
                        +" cv:employedIn "
                        +" <"+experience.getCompanyLink()+">; "

                        +" cv:jobTitle "
                        +"\""+ experience.getJobTitle() +"\";"

                        + " cv:startDate "
                        + "\""+ experience.getStartDate() +"\";"

                        + " cv:endDate "
                        + "\""+ experience.getEndDate() +"\"."

                        +" }";

        execUpdate(strQuery);

    }

    public List<String> getAllCompanies(){

        String strQuery =
                "PREFIX dbo: "+ ConfStrings.dbo

                        +"SELECT DISTINCT ?company "
                        +"WHERE {"
                        +" ?company a dbo:Company "
                        +"} ";

        QueryExecution queryExecution = QueryExecutionFactory
                .           sparqlService(ConfStrings.SPARQL_ENDPOINT, strQuery);
        ResultSet resultSet = queryExecution.execSelect();

        //Map<name,Resource>
        List<String> nameResource = new ArrayList<>();

        while (resultSet.hasNext()){
            QuerySolution qs = resultSet.next();

            nameResource.add(qs.get("company").asResource().getURI());
        }

        queryExecution.close();
        return nameResource;
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
