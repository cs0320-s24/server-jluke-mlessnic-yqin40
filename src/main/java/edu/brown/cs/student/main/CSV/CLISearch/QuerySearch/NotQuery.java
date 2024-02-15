package edu.brown.cs.student.main.CSV.CLISearch.QuerySearch;

import edu.brown.cs.student.main.CLISearch.Exceptions.SearchArgumentErrorException;
import edu.brown.cs.student.main.CLISearch.Searcher;

import java.util.List;

public class NotQuery implements Query {

    Query childQuery;
    String text;

    public NotQuery(String text) {
        this.text = text;
        //create childQuery
        try {
            this.parseText(text);
        } catch (SearchArgumentErrorException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void parseText(String text) throws SearchArgumentErrorException {
        QueryBuilder queryBuilder = new QueryBuilder(text);
        Query childQuery = queryBuilder.build();
    }


    @Override
    public List<Integer> findRows(Searcher searcher) {
        List<Integer> pool = searcher.getPool();
        List<Integer> q1rowIndices = childQuery.findRows(searcher);

        pool.removeAll(q1rowIndices);
        return pool;

    }

    @Override
    public String getText() {
        return this.text;
    }
}
