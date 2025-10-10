<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.GregorianCalendar, java.util.Calendar" %>
<footer>
    <%
        GregorianCalendar currentDate = new GregorianCalendar();
        int currentYear = currentDate.get(Calendar.YEAR);
    %>
    <p>&copy; Copyright <%= currentYear %> Mike Murach &amp; Associates</p>
</footer>
</body>
</html>