package com.example.globalbeershop.BeerStocked;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class BeerStockedRepositry {

    @Autowired
    JdbcTemplate jdbcTemplate;

    class BeerStockedRowMapper implements RowMapper<BeerStocked> {

        @Override
        public BeerStocked mapRow(ResultSet rs, int rowNum) throws SQLException {
            BeerStocked beer = new BeerStocked
            (
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("country"),
                rs.getString("brewer"),
                rs.getString("type"),
                rs.getDouble("abv"),
                rs.getDouble("price"),
                rs.getString("image"),
                rs.getString("description")
            );

            return beer;
        }

    }

    class DistinctColRowMapper implements RowMapper<String> {

        private String col;

        public DistinctColRowMapper(String col){
            super();
            this.col = col;
        }

        @Override
        public String mapRow(ResultSet rs, int rowNum) throws SQLException {
            if(col=="abv") return ((Double)rs.getDouble("abv")).toString();
            else return rs.getString(col);
        }

    }

    public List<BeerStocked> findAll(List<String> sort) {

        //checks sorting requirements have been given
        if(sort.size() == 2){

            List<String> validSortOrd = new ArrayList<String>() {{add("ASC"); add("DESC");}};
            List<String> validSortCol = new ArrayList<String>() {{add("country"); add("type");add("abv"); add("brewer"); add("name");}};

            //checks if the requirement args are valid
            if (sort.size() == 2) if (!validSortOrd.contains(sort.get(1)) || !validSortCol.contains(sort.get(0))) return new ArrayList<BeerStocked>();

        }
        return jdbcTemplate.query("select * from beerStocked ORDER BY "+sort.get(0)+ " " + sort.get(1), new BeerStockedRowMapper());
    }

    public BeerStocked findById(String beerId) {
        return jdbcTemplate.queryForObject("select * from beerStocked where id=?", new Object[] { beerId },
                new BeerStockedRowMapper());
    }


    public List<BeerStocked> findByColumn(List<String> cols, List<Object> vals, List<String> sort){

        /*VALIDATES ARGS
            - At least one col and one val must be given
            - The the number of cols = number of vals
            - If order has 2 items, second item is either "ASC" or "DESC" (respectively)
            - If order has 2 items, the first item is only from an allowed set of columns
         */

        List<String> validSortOrd = new ArrayList<String>() {{add("ASC"); add("DESC");}};
        List<String> validSortCol = new ArrayList<String>() {{add("country"); add("type");add("abv"); add("brewer"); add("name");}};

        if(cols.size() < 1 || vals.size() < 1 || vals.size() != cols.size()){
            return null;
        }
        else if (sort.size() == 2){
            if (!validSortOrd.contains(sort.get(1)) || !validSortCol.contains(sort.get(0))) return null;
        }


        //the basic sql statement and query values for a one specified column
        String sql = "select * from beerStocked where " + cols.get(0) + "=?";
        Object[] queryVals = new Object[vals.size()];
        queryVals[0] = vals.get(0);

        //if more than one columns are specified
        if(cols.size()>1){

            //for the remaining column/value pairs, finishes the SQL query and collects up values to be used
            for(int i = 1; i<cols.size(); i++){
                sql+=" AND " + cols.get(i) + "=?";
                queryVals[i] = vals.get(i);
            }
        }





        //apply ordering on results, if specified
        if(!sort.isEmpty()) sql = sql + " ORDER BY " + sort.get(0) +  " " + sort.get(1);

        //execute sql query
        List<BeerStocked> searchResults = jdbcTemplate.query(sql, queryVals, new BeerStockedRowMapper());

        return searchResults;
    }



    public List<String> getAllDisinctCol(String col) {

        return jdbcTemplate.query("SELECT DISTINCT " + col + " FROM BeerStocked ORDER BY "+ col +" ASC", new DistinctColRowMapper(col));


    }

    public int reduceAvailableStock(String beerId, String quantity){
        return jdbcTemplate.update("update beerStocked set availableStock = availableStock - ? where id = ?",
                new Object[] {quantity, beerId});
    }


    public int reduceActualStock(String beerId, String quantity){
        return jdbcTemplate.update("update beerStocked set actualStock = actualStock - ? where id = ?",
                new Object[] {quantity, beerId});
    }
}
