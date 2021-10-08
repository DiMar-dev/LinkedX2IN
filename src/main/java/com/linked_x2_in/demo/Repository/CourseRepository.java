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
public class CourseRepository {

    private static Logger logger = LoggerFactory.getLogger(LinkedUserRepository.class);
    // Why This Failure marker
    private static final Marker WTF_MARKER = MarkerFactory.getMarker("WTF");

    public void addCourse(String CV_URI, Course course){
        String strQuery =
                " PREFIX xsd: "+ConfStrings.xsd
                        +" PREFIX cv: "+ConfStrings.resume
                        +" INSERT DATA {"

                        +" <"+CV_URI+"> "
                        +" cv:hasCourse "
                        +" <"+CV_URI+"/Course/"+course.getCourseName()+">. "

                        +" <"+CV_URI+"/Course/"+course.getCourseName()+"> "
                        +" cv:courseTitle "
                        +" \""+course.getCourseName()+"\"^^xsd:string; "

                        +" cv:courseURL "
                        +" \""+course.getCourseLink()+"\"^^xsd:string; "

                        + " cv:courseStartDate "
                        + "\""+ course.getStartDate() +"\"^^xsd:date;"

                        + " cv:courseFinishDate "
                        + "\""+ course.getEndDate() +"\"^^xsd:date."

                        +" }";

        execUpdate(strQuery);
    }

    public List<Course> findAllByCV (String CV_URI){

        String strQuery =
                " PREFIX cv: "+ConfStrings.resume
                        +" SELECT ?name ?link ?start ?end "
                        +"WHERE { "

                        +" <"+CV_URI+"> "
                        +" cv:hasCourse "
                        +" ?uri."

                        +" ?uri"
                        +" cv:courseTitle "
                        +" ?name;"

                        +" cv:courseURL "
                        +" ?link;"

                        + " cv:courseStartDate "
                        + " ?start;"

                        + " cv:courseFinishDate "
                        + " ?end."

                        +" }";

        QueryExecution queryExecution = QueryExecutionFactory
                                    .sparqlService(ConfStrings.REPO_QUERY, strQuery);
        ResultSet resultSet = queryExecution.execSelect();

        List<Course> result = new ArrayList<>();
        while (resultSet.hasNext()){
            QuerySolution qs = resultSet.next();

            result.add(Course.builder()
                    .courseName(qs.get("name").asLiteral().getString())
                    .courseLink(qs.get("link").asLiteral().getString())
                    .startDate(qs.get("start").asLiteral().getString())
                    .endDate(qs.get("end").asLiteral().getString())
                    .build());
        }

        queryExecution.close();
        return result;


    }

    public void deleteCourse(String CV_URI, String courseURI, Course course){

        String strQuery =
                " PREFIX cv: "+ConfStrings.resume
                        +"DELETE DATA { "

                        +" <"+CV_URI+"> "
                        +" cv:hasCourse "
                        +" <"+courseURI+">."

                        +" <"+courseURI+">"
                        +" cv:courseTitle "
                        + "\"" + course.getCourseName() +"\";"

                        +" cv:courseURL "
                        + "\"" + course.getCourseLink() +"\";"

                        + " cv:courseStartDate "
                        + "\"" + course.getStartDate() +"\";"

                        + " cv:courseFinishDate "
                        + "\"" + course.getEndDate() +"\"."

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
