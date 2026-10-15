
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/workouts")
public class WorkoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String selectedPlan = request.getParameter("plan");
        String contextPath = request.getContextPath();

        try (PrintWriter out = response.getWriter()) {

            out.println("<!DOCTYPE html>");
            out.println("<html><head><meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>FitZone - Workout Plans</title>");

            out.println("<style>");
            out.println("body{background:#111;color:white;font-family:Arial,sans-serif;padding:30px;margin:0}");
            out.println("h1{color:#65e6a5;text-align:center}");
            out.println(".container{max-width:1000px;margin:30px auto}");
            out.println(".cards{display:grid;grid-template-columns:repeat(auto-fit,minmax(230px,1fr));gap:20px}");
            out.println(".card{background:#202020;padding:25px;border-radius:12px;border:1px solid #333}");
            out.println(".card h2{color:#65e6a5}");
            out.println(".button{display:inline-block;background:#65e6a5;color:#111;padding:12px 18px;border-radius:6px;text-decoration:none;font-weight:bold;margin-top:10px}");
            out.println(".button:hover{background:#8cffc3}");
            out.println(".details{background:#202020;padding:25px;border-radius:12px;line-height:1.8}");
            out.println("a.back{color:#65e6a5}");
            out.println("</style></head><body>");

            out.println("<div class='container'>");
            out.println("<h1>FitZone - Workout Plans</h1>");

            if (selectedPlan != null && !selectedPlan.isBlank()) {

                out.println("<div class='details'>");
                out.println("<h2>" + escapeHtml(selectedPlan) + "</h2>");

                if ("Weight Loss".equalsIgnoreCase(selectedPlan)) {
                    out.println("<h3>Workout Routine</h3>");
                    out.println("<p>Jumping Jacks: 3 sets x 30 seconds</p>");
                    out.println("<p>Bodyweight Squats: 3 sets x 15 reps</p>");
                    out.println("<p>Mountain Climbers: 3 sets x 20 reps</p>");
                    out.println("<p>Brisk Walking or Jogging: 15 minutes</p>");
                    out.println("<p>Rest: 45-60 seconds between sets</p>");

                } else if ("Muscle Building".equalsIgnoreCase(selectedPlan)) {
                    out.println("<h3>Workout Routine</h3>");
                    out.println("<p>Push-ups: 3 sets × 8-12 reps</p>");
                    out.println("<p>Bodyweight Squats: 3 sets × 12 reps</p>");
                    out.println("<p>Backpack Rows: 3 sets × 10 reps</p>");
                    out.println("<p>Plank: 3 sets × 30 seconds</p>");
                    out.println("<p>Rest: 60–90 seconds between sets</p>");

                } else if ("General Fitness".equalsIgnoreCase(selectedPlan)) {
                    out.println("<h3>Workout Routine</h3>");
                    out.println("<p>Marching in Place: 3 sets × 1 minute</p>");
                    out.println("<p>Bodyweight Squats: 3 sets × 10 reps</p>");
                    out.println("<p>Wall Push-ups: 3 sets × 10 reps</p>");
                    out.println("<p>Plank: 3 sets × 20 seconds</p>");
                    out.println("<p>Rest: 45–60 seconds between sets</p>");

                } else {
                    out.println("<p>No exercise routine is available for this plan yet.</p>");
                }

                out.println("<p>Warm up before exercising and stop if you feel pain or dizziness.</p>");
                out.println("<a class='back' href='" + contextPath + "/workouts'>Back to all plans</a>");
                out.println("</div>");

            } else {

                out.println("<div class='cards'>");

                String sql = "SELECT title, description, duration FROM WorkoutPlans";

                try (Connection con = DBConnection.getConnection();
                     PreparedStatement ps = con.prepareStatement(sql);
                     ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {
                        String title = rs.getString("title");
                        String description = rs.getString("description");
                        String duration = rs.getString("duration");

                        String planUrl = contextPath + "/workouts?plan="
                                + URLEncoder.encode(title, StandardCharsets.UTF_8);

                        out.println("<div class='card'>");
                        out.println("<h2>" + escapeHtml(title) + "</h2>");
                        out.println("<p>" + escapeHtml(description) + "</p>");
                        out.println("<p>Duration: " + escapeHtml(duration) + "</p>");
                        out.println("<a class='button' href='" + planUrl + "'>View Exercises</a>");
                        out.println("</div>");
                    }

                } catch (Exception e) {
                    getServletContext().log("Unable to load workout plans", e);
                    out.println("<p>Unable to load workout plans. Check the database connection.</p>");
                }

                out.println("</div>");
                out.println("<p style='text-align:center;margin-top:30px'>");
                out.println("<a class='back' href='" + contextPath + "/index.html'>Back to Home</a>");
                out.println("</p>");
            }

            out.println("</div></body></html>");
        }
    }

    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }

        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
}

