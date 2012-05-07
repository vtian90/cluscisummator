<%-- 
    Document   : index
    Created on : 14 Apr 12, 23:04:17
    Author     : Akbar Gumbira (akbargumbira@gmail.com)
--%>

<%@page import="java.io.File"%>
<%@page import="java.util.ArrayList"%>
<%@page import="core.utility.Global" %>

<%
    ArrayList<String> listOfPapersTitle = (ArrayList<String>) request.getAttribute("listOfPapersTitle");    
%>


<div class="one_column">
    <div class="home">
        <div class="input_file">
            <h2>Upload Paper</h2>
            <form action="upload" method="POST" enctype="multipart/form-data" id="upload_form">
                <table>
                    <tr>
                        <input type="file" name="paper">
                    </tr>
                    <tr>
                        <td> <input type="submit" value="Upload"> </td>
                    </tr>
                </table>
            </form>
        </div>   
        </br>
        
        <% if (listOfPapersTitle.size()!=0) { %>
            <div class="uploaded_file" id="uploaded_file">
                <h2>Uploaded Papers: </h2>
                <%
                    for (int i = 0; i < listOfPapersTitle.size(); ++i) {
                %>  <label> <%=i+1 %>. <%= listOfPapersTitle.get(i) %> </label> </br>
                    <%};
                %>

                <table>
                    <tr>
                        <td> 
                            <form action="result" method="GET">
                                <input type="submit" value="Summarize!"></input>
                            </form>
                        </td>
                        <td>
                            <form action="clear_papers" method="POST">
                                <input type="submit" value="Clear Papers!"></input>
                            </form>   
                        </td>
                    </tr>
                </table>
            </div>
        <% } %>                
    </div>
</div>
    
<script>
    $(document).ready(function() {         
          $("#upload_form").validate({
            rules : {
                paper : {
                    required : true
                }
            }
        });
    });
</script>