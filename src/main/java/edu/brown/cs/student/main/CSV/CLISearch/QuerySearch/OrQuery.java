package edu.brown.cs.student.main.CSV.CLISearch.QuerySearch;

import edu.brown.cs.student.main.CLISearch.Exceptions.SearchArgumentException;
import edu.brown.cs.student.main.CLISearch.Searcher;

import java.util.List;

public class OrQuery implements Query {

    Query q1;
    Query q2;
    String text;

    public OrQuery() {
    }

    public OrQuery(String text) {
        this.text = text;

        //create two child Queries
        try {
            this.parseText(text);
        } catch (SearchArgumentException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public void parseText(String text) throws SearchArgumentException {
        //separate text at the comma that is not inside the inner parentheses

        List<String> args = this.separateString(text);
        if (args.size() > 2) {
            throw new SearchArgumentException("Too many arguments for OrQuery");
        }
        String arg1 = args.get(0);
        String arg2 = args.get(1);

        QueryBuilder queryBuilder1 = new QueryBuilder(arg1);
        QueryBuilder queryBuilder2 = new QueryBuilder(arg2);

        this.q1 = queryBuilder1.build();
        this.q2 = queryBuilder2.build();

    }

    @Override
    public List<Integer> findRows(Searcher searcher) {
        List<Integer> q1rowIndices = q1.findRows(searcher);
        List<Integer> q2rowIndices = q2.findRows(searcher);

        q1rowIndices.addAll(q2rowIndices);
        return q1rowIndices;

    }

    @Override
    public String getText() {
        return this.text;
    }


    public Query getQ1() {
        return q1;
    }

    public Query getQ2() {
        return q2;
    }



}
