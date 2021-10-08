package com.linked_x2_in.demo.Repository;

import com.linked_x2_in.demo.DTOs.Course;
import com.linked_x2_in.demo.DTOs.Education;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class EducationRepository {

    private static Logger logger = LoggerFactory.getLogger(LinkedUserRepository.class);
    // Why This Failure marker
    private static final Marker WTF_MARKER = MarkerFactory.getMarker("WTF");


    public void addEducation(String CV_URI, Education education){
        String strQuery =
                " PREFIX xsd: "+ConfStrings.xsd
                        +" PREFIX cv: "+ConfStrings.resume
                        +" INSERT DATA {"

                        +" <"+CV_URI+"> "
                        +" cv:hasEducation "
                        +" <"+CV_URI+"/Education/"+education.getSchoolName()+">. "

                        +" <"+CV_URI+"/Education/"+education.getSchoolName()+"> "
                        +" cv:studiedIn "
                        +" <"+education.getSchoolLink()+">; "

                        + " cv:eduStartDate "
                        + "\""+ education.getStart() +"\"^^xsd:date;"

                        + " cv:eduGradDate "
                        + "\""+ education.getGraduate() +"\"^^xsd:date."

                        +" }";

        execUpdate(strQuery);
    }

    public List<Education> findAllByCV(String CV_URI){

        String strQuery =
                " PREFIX cv: "+ConfStrings.resume
                        +" SELECT  ?link ?start ?end "
                        +"WHERE { "

                        +" <"+CV_URI+"> "
                        +" cv:hasEducation "
                        +" ?uri."

                        +" ?uri"
                        +" cv:studiedIn "
                        +" ?link;"

                        + " cv:eduStartDate "
                        + " ?start;"

                        + " cv:eduGradDate "
                        + " ?end."

                        +" }";


        QueryExecution queryExecution = QueryExecutionFactory
                                .sparqlService(ConfStrings.REPO_QUERY, strQuery);
        ResultSet resultSet = queryExecution.execSelect();

        List<Education> result = new ArrayList<>();
        while (resultSet.hasNext()){
            QuerySolution qs = resultSet.next();

            result.add(Education.builder()
                    .schoolName(qs.get("link").asResource().getLocalName())
                    .schoolLink(qs.get("link").asResource().getURI())
                    .start(qs.get("start").asLiteral().getString())
                    .graduate(qs.get("end").asLiteral().getString())
                    .build());
        }

        queryExecution.close();
        return result;

    }


    public void delete(String CV_URI, String eduURI, Education education){

        String strQuery =
                " PREFIX cv: "+ConfStrings.resume
                        +"DELETE DATA { "

                        +" <"+CV_URI+"> "
                        +" cv:hasEducation "
                        +" <"+eduURI+">. "

                        +" <"+eduURI+"> "
                        +" cv:studiedIn "
                        +" <"+education.getSchoolLink()+">; "

                        + " cv:eduStartDate "
                        +"\""+ education.getStart() +"\";"

                        + " cv:eduGradDate "
                        +"\""+ education.getGraduate() +"\"."

                        +" }";

        execUpdate(strQuery);
    }

    public List<String> getAllEducationalInstitutions(){

        String strQuery =
                "PREFIX dbo: "+ ConfStrings.dbo

                +"SELECT DISTINCT ?education "
                +"WHERE {"
                +" {?s  dbo:university ?education} "
                +      " UNION "
                +" {?s  dbo:school ?education} "
                +       " UNION "
                +" {?s  dbo:college ?education} "
                +"} ";

        QueryExecution queryExecution = QueryExecutionFactory
                .           sparqlService(ConfStrings.SPARQL_ENDPOINT, strQuery);
        ResultSet resultSet = queryExecution.execSelect();

        List<String> nameResource = new ArrayList<>();

        while (resultSet.hasNext()){
            QuerySolution qs = resultSet.next();

            nameResource.add(qs.get("education").asResource().getURI());
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
