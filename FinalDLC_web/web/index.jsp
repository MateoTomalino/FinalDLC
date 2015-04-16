<%@page import="finaldlc.TagCloud"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Oven Search Engine</title>
        <link rel="icon" type="image/x-icon" href="images/favicon.png"></li>
    <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="css/styles.css">
</head>
<body>

    <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
        <div class="container">
            <div class="col-md-4">
                <img class="logo" src="images/oven.png">
                <h2>Oven Search Engine</h2>
            </div>
            <div class="col-md-8 searchZone">
                <h4>From a kitchen, to the world ..</h4>
                <form action="BuscadorController" class="navbar-form navbar-left" role="search">
                    <div class="form-group">
                        <input name="consulta" type="text" class="form-control" placeholder="Type to search">
                    </div>
                    <input type="hidden" name="pagina" value="1" />
                    <button type="submit" class="btn btn-default">Search</button>
                </form>
            </div>
        </div>
    </nav>
    
        <%
            String tagCloud = (String) TagCloud.getInstance().toString();
            if (request.getParameter("consulta") == null) {
                out.print(tagCloud);
            }
        %>

    <div id="resultado" class="col-md-12 results">
        <%
            String html = (String) request.getAttribute("resultados");
            String paginado = (String) request.getAttribute("paginado");
            if (html != null && html != "") {
                out.print(html);
                out.print(paginado);
            }
        %>
    </div>
    
    
    <script src="http://code.jquery.com/jquery-1.10.2.js"></script>
    <script type="text/javascript" src="js/tagCloud.js"></script>
    <script type="text/javascript" src="js/script.js"></script>        
</body>
</html>