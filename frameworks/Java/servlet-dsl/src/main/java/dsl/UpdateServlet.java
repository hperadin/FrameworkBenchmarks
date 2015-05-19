package dsl;

import java.io.IOException;
import java.sql.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.dslplatform.client.json.JsonWriter;

import dsl.Bench.World;

public class UpdateServlet extends HttpServlet {
    private static final String READ_QUERY = "SELECT randomNumber FROM World WHERE id = ?";
	private static final String UPDATE_QUERY_TEMPLATE = "UPDATE World w SET randomNumber = ds.randomNumber FROM unnest(#{arrayOfWorlds}::World[]) ds WHERE w.id = ds.id";

    @Resource(name = "jdbc/data_source")
    private DataSource dataSource;

	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
		int count = 1;
		try {
			count = Integer.parseInt(req.getParameter("queries"));
			if (count > 500) {
				count = 500;
			} else if (count < 1) {
				count = 1;
			}
		} catch (final NumberFormatException ignore) {
		}

		final World[] worlds = new World[count];
		final Random random = ThreadLocalRandom.current();

        try (final Connection conn = dataSource.getConnection()) {
			try (final PreparedStatement psRead = conn.prepareStatement(READ_QUERY,
					ResultSet.TYPE_FORWARD_ONLY,
					ResultSet.CONCUR_READ_ONLY)) {
				//TODO: this loop doesn't make sense in real world. You would aggregate those multiple queries into a single DB call.
				for (int i = 0; i < count; i++) {
					final int id = random.nextInt(10000) + 1;
					psRead.setInt(1, id);

					try (final ResultSet results = psRead.executeQuery()) {
						if (results.next()) {
							//TODO: NOOP ;(
							worlds[i] = new World(id, results.getInt(1));
							worlds[i].setRandomNumber(random.nextInt(10000) + 1);
						}
					}
				}
			}
			//Bulk update
			try (PreparedStatement pgUpdate = createUpdateQuery(worlds, conn)) {
				System.out.println(pgUpdate.toString());
				pgUpdate.execute();
			}
		} catch (final SQLException ex) {
			ex.printStackTrace();
		}

		final JsonWriter writer = Utils.getJson();
		//writer.
		//writer.ser
		res.setContentType(Utils.JSON_CONTENT);
		// Write JSON encoded message to the response.
		//CCommon.MAPPER.writeValue(res.getOutputStream(), worlds);
	}

	private static PreparedStatement createUpdateQuery(final World[] worlds, Connection conn) throws SQLException {
		return conn.prepareStatement(UPDATE_QUERY_TEMPLATE.replace("#{arrayOfWorlds}", convertToArray(worlds)));
	}

	private static String convertToArray(final World[] worlds) {
		final StringBuilder sb = new StringBuilder(worlds.length * 10);
		sb.append("ARRAY[(");
		final World w = worlds[0];
		sb.append(w.getId());
		sb.append(",");
		sb.append(w.getRandomNumber());
		for (int i = 1; i < worlds.length; i++) {
			sb.append("),(");
			sb.append(w.getId());
			sb.append(",");
			sb.append(w.getRandomNumber());
		}
		sb.append(")]");
		return sb.toString();
	}
}
