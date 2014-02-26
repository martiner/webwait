import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@WebServlet(urlPatterns = WaitServlet.URI, loadOnStartup = 1)
public class WaitServlet extends HttpServlet {

	static final String URI = "/wait";
	private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
	private static final String PARAM = "c";
	private static final int DEFAULT_TIMEOUT = 10;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		log("Servlet accessible at " +
				config.getServletContext().getContextPath() + URI + "?" + PARAM + "=" + DEFAULT_TIMEOUT);
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		final String c = req.getParameter(PARAM);
		Integer timeout;
		try {
			timeout = Integer.valueOf(c);
		} catch (NumberFormatException e) {
			timeout = DEFAULT_TIMEOUT;
		}
		try {
			log("Received " + req.getMethod() + " request, sleeping for " + timeout + " " + TIME_UNIT);
			TIME_UNIT.sleep(timeout);
			resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
		} catch (InterruptedException e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
}
