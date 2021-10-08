package com.linked_x2_in.demo.Repository;

import com.linked_x2_in.demo.DTOs.LinkedUser;
import org.apache.jena.query.*;
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
public class LinkedUserRepository {

    private static Logger logger = LoggerFactory.getLogger(LinkedUserRepository.class);
    // Why This Failure marker
    private static final Marker WTF_MARKER = MarkerFactory.getMarker("WTF");


    public void addUser(LinkedUser linkedUser){

        String strInsert =
                "PREFIX rdf: "+ConfStrings.rdf
                        +" PREFIX foaf: "+ConfStrings.foaf
                        +" PREFIX purl: "+ConfStrings.purl
                        +" PREFIX dbo: "+ConfStrings.dbo
                        +" PREFIX xsd: "+ConfStrings.xsd
                +" INSERT DATA {"
                        + "<http://LinkedX2IN.com/Profile/"+ linkedUser.getFirstName().concat("_")
                                                                .concat(linkedUser.getLastName()) +"> "
                        + " rdf:type "
                        + " dbo:Person;"

                        + " foaf:givenName "
                        + "\""+ linkedUser.getFirstName() +"\"^^xsd:string;"

                        + " foaf:familyName "
                        + "\""+ linkedUser.getLastName() +"\"^^xsd:string;"

                        + " foaf:gender "
                        + "\""+ linkedUser.getGender() +"\"^^xsd:string;"

                        + " purl:description "
                        + "\""+ linkedUser.getDescription() +"\"^^xsd:string;"

                        + " foaf:mbox "
                        + "<mailto:"+ linkedUser.getMail() +">;"

                        + " foaf:img "
                        + "<"+ linkedUser.getImage() +">;"

                        + " dbo:birthDate "
                        + "\""+ linkedUser.getDateOfBirth() +"\"^^xsd:date. }";

        execUpdate(strInsert);

    }

    public void connect(String  userURI,String connectionURI){

        String strInsert =
            "PREFIX rdf: "+ConfStrings.rdf
                    +" PREFIX foaf: "+ConfStrings.foaf
                    +" INSERT DATA {"
                    +" <"+userURI+">"
                    +" foaf:knows "
                    +"<"+connectionURI+">. }";

        execUpdate(strInsert);


    }

    public List<LinkedUser> findAllConnections(String  userURI){

        String strQuery =
                "PREFIX rdf: "+ConfStrings.rdf
                        +" PREFIX foaf: "+ConfStrings.foaf
                        +" PREFIX purl: "+ConfStrings.purl
                        +" PREFIX dbo: "+ConfStrings.dbo
                        +" PREFIX xsd: "+ConfStrings.xsd
                        +" SELECT ?link ?name ?surname ?gender ?description ?mbox ?img ?birthDate "
                        +"WHERE { "

                        +" ?link "
                        +" foaf:knows "
                        +"<"+userURI+">;"

                        + " foaf:givenName "
                        + " ?name; "

                        + " foaf:familyName "
                        + " ?surname; "

                        + " foaf:gender"
                        + " ?gender;"

                        + " purl:description"
                        + " ?description;"

                        + " foaf:mbox"
                        + " ?mbox;"

                        + " foaf:img"
                        + " ?img;"

                        + " dbo:birthDate"
                        + " ?birthDate. }";

        QueryExecution queryExecution = QueryExecutionFactory
                                    .sparqlService(ConfStrings.REPO_QUERY, strQuery);
        ResultSet resultSet = queryExecution.execSelect();

        List<LinkedUser> result = new ArrayList<>();
        while (resultSet.hasNext()){
            QuerySolution qs = resultSet.next();

            result.add(LinkedUser.builder()
                    .URI(qs.get("?link").asResource().getURI())
                    .firstName(qs.get("?name").asLiteral().getString())
                    .lastName(qs.get("?surname").asLiteral().getString())
                    .gender(qs.get("?gender").asLiteral().toString())
                    .description(qs.get("?description").asLiteral().toString())
                    .mail(qs.get("?mbox").asResource().toString())
                    .image(qs.get("?img").toString())
                    .dateOfBirth(qs.get("?birthDate").asLiteral().getString())
                    .build());
        }

        queryExecution.close();
        return result;


    }


    public List<LinkedUser> findAll(){

        String strQuery =
                "PREFIX rdf: "+ConfStrings.rdf
                        +" PREFIX foaf: "+ConfStrings.foaf
                        +" PREFIX purl: "+ConfStrings.purl
                        +" PREFIX dbo: "+ConfStrings.dbo
                        +" PREFIX xsd: "+ConfStrings.xsd
                        +" SELECT ?link ?name ?surname ?gender ?description ?mbox ?img ?birthDate "
                        +"WHERE { ?link rdf:type dbo:Person;"

                        + " foaf:givenName "
                        + " ?name; "

                        + " foaf:familyName "
                        + " ?surname; "

                        + " foaf:gender"
                        + " ?gender;"

                        + " purl:description"
                        + " ?description;"

                        + " foaf:mbox"
                        + " ?mbox;"

                        + " foaf:img"
                        + " ?img;"

                        + " dbo:birthDate"
                        + " ?birthDate. }";

        QueryExecution queryExecution = QueryExecutionFactory
                                        .sparqlService(ConfStrings.REPO_QUERY, strQuery);
        ResultSet resultSet = queryExecution.execSelect();

        List<LinkedUser> result = new ArrayList<>();
        while (resultSet.hasNext()){
            QuerySolution qs = resultSet.next();

            result.add(LinkedUser.builder()
                    .URI(qs.get("?link").asResource().getURI())
                    .firstName(qs.get("?name").asLiteral().getString())
                    .lastName(qs.get("?surname").asLiteral().getString())
                    .gender(qs.get("?gender").asLiteral().toString())
                    .description(qs.get("?description").asLiteral().toString())
                    .mail(qs.get("?mbox").asResource().toString())
                    .image(qs.get("?img").toString())
                    .dateOfBirth(qs.get("?birthDate").asLiteral().getString())
                    .build());
        }

        queryExecution.close();
        return result;


    }


    public LinkedUser getUserByNameSurname(String nameSurname){

        String strQuery =
                "PREFIX rdf: "+ConfStrings.rdf
                        +" PREFIX foaf: "+ConfStrings.foaf
                        +" PREFIX purl: "+ConfStrings.purl
                        +" PREFIX dbo: "+ConfStrings.dbo
                        +" PREFIX xsd: "+ConfStrings.xsd
                +" SELECT ?gender ?description ?mbox ?img ?birthDate "
                        +"WHERE { <http://LinkedX2IN.com/Profile/"+ nameSurname +"> "

                        + " foaf:gender"
                        + " ?gender;"

                        + " purl:description"
                        + " ?description;"

                        + " foaf:mbox"
                        + " ?mbox;"

                        + " foaf:img"
                        + " ?img;"

                        + " dbo:birthDate"
                        + " ?birthDate. }";

        QueryExecution queryExecution = QueryExecutionFactory
                                    .sparqlService(ConfStrings.REPO_QUERY, strQuery);
        ResultSet resultSet = queryExecution.execSelect();

        QuerySolution qs = resultSet.next();
        return LinkedUser.builder()
                .URI("http://LinkedX2IN.com/Profile/"+ nameSurname)
                .firstName(nameSurname.split("_")[0])
                .lastName(nameSurname.split("_")[1])
                .gender(qs.get("?gender").asLiteral().toString())
                .description(qs.get("?description").asLiteral().toString())
                .mail(qs.get("?mbox").toString())
                .image(qs.get("?img").toString())
                .dateOfBirth(qs.get("?birthDate").asLiteral().getString())
                .build();
    }


    public void deleteUSer(LinkedUser linkedUser){

        String strInsert =
                "PREFIX rdf: "+ConfStrings.rdf
                        +" PREFIX foaf: "+ConfStrings.foaf
                        +" PREFIX purl: "+ConfStrings.purl
                        +" PREFIX dbo: "+ConfStrings.dbo
                        +" PREFIX xsd: "+ConfStrings.xsd
                        +" DELETE DATA {"
                        + "<http://LinkedX2IN.com/Profile/"+ linkedUser.getFirstName().concat("_")
                        .concat(linkedUser.getLastName()) +"> "
                        + " rdf:type "
                        + " dbo:Person;"

                        + " foaf:givenName "
                        + "\""+ linkedUser.getFirstName() +"\"^^xsd:string;"

                        + " foaf:familyName "
                        + "\""+ linkedUser.getLastName() +"\"^^xsd:string;"

                        + " foaf:gender "
                        + "\""+ linkedUser.getGender() +"\"^^xsd:string;"

                        + " purl:description "
                        + "\""+ linkedUser.getDescription() +"\"^^xsd:string;"

                        + " foaf:mbox "
                        + "<mailto:"+ linkedUser.getMail() +">;"

                        + " foaf:img "
                        + "<"+ linkedUser.getImage() +">;"

                        + " dbo:birthDate "
                        + "\""+ linkedUser.getDateOfBirth() +"\"^^xsd:date. }";

        execUpdate(strInsert);

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
