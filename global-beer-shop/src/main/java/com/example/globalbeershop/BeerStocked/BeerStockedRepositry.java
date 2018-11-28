package com.example.globalbeershop.BeerStocked;

import java.sql.ResultSet;
        import java.sql.SQLException;
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
            BeerStocked beer = new BeerStocked();
            beer.setId(rs.getLong("id"));
            beer.setName(rs.getString("name"));
            beer.setOrigin(rs.getString("origin"));
            beer.setBrewer(rs.getString("brewer"));
            beer.setType(rs.getString("type"));
            beer.setAbv(rs.getDouble("abv"));

            return beer;
        }

    }

    public List<BeerStocked> findAll() {
        return jdbcTemplate.query("select * from beerStocked", new BeerStockedRowMapper());
    }

    public BeerStocked findById(long id) {
        return jdbcTemplate.queryForObject("select * from beerStocked where id=?", new Object[] { id },
                new BeerStockedRowMapper());
    }

    public int deleteById(long id) {
        return jdbcTemplate.update("delete from beerStocked where id=?", new Object[] { id });
    }

    public int insert(BeerStocked beer) {
        return jdbcTemplate.update("insert into beerStocked (id, name, origin, brewer, type, abv) " + "values(?,  ?, ?)",
                new Object[] { beer.getId(), beer.getName(), beer.getOrigin(), beer.getBrewer(), beer.getType(), beer.getAbv()});
    }

    public int update(BeerStocked beer) {
        return jdbcTemplate.update("update beerStocked " + " set name = ?, origin = ? , brewer = ?, type = ?, abv = ?" + " where id = ?",
                new Object[] { beer.getName(), beer.getOrigin(), beer.getBrewer(), beer.getType(), beer.getAbv(), beer.getId()});
    }

}
