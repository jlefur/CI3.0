//This source code is licensed under Creative Common By license as detailed in file CI3.0.license.txt
// test
package presentation;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import business.C_DescriptorSimple;
import business.C_Filter;

/** Class processing requests for filtering a list of information */
@SuppressWarnings("deprecation")
public class C_ServletFilter extends HttpServlet {

	// FIELDS (2)
	private static final long serialVersionUID = 1L;
	PrintWriter sortie;

	// SPECIFIC METHOD
	/** Method of processing "Get" type requests from a client */
	public void doGet(HttpServletRequest requete, HttpServletResponse reponse) throws ServletException {
		try {
			reponse.setContentType("text/html");
			sortie = reponse.getWriter();
			String type = requete.getParameter("type");
			String contenu = requete.getParameter("contenu");
			HttpSession session = requete.getSession(true);

			if (type.equalsIgnoreCase("AnnulerFiltre")) {
				C_DescriptorSimple ds = (C_DescriptorSimple) session.getAttribute("lastList");
				session.invalidate();
				sortie.println("<html>");
				sortie.println("<body onLoad=\"window.location='GetList.html" + "?type="
						+ URLEncoder.encode(ds.getDescriptorType()) + "&contenu="
						+ URLEncoder.encode(ds.getDescriptorName()) + "'\" ></body>");
				sortie.println("</html>");

			}
			else {
				C_Filter filtre = new C_Filter(type, contenu);
				session.setAttribute("filtre", filtre);
				sortie.println("<html>");
				sortie.println("<body onLoad=\"window.location='GetList.html" + "?type=" + type + "&contenu="
						+ contenu + "'\" ></body>");
				sortie.println("</html>");
			}
		} catch (IOException ioe) {
			throw new ServletException(ioe);
		}
	}
}
