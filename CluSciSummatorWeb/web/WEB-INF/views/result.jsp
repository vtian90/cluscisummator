<%-- 
    Document   : index
    Created on : 15 Apr 12, 09:01:26
    Author     : Akbar Gumbira (akbargumbira@gmail.com)
--%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Hashtable" %>


<% Hashtable<ArrayList<String>, ArrayList<String>> summarization = (Hashtable<ArrayList<String>, ArrayList<String>>) request.getAttribute("summarization"); %>
<% String tes = (String) request.getAttribute("tes"); %>
        

<div class="summarization_result" id="result">
      <% Enumeration clusters = summarization.keys(); %>
        <% while (clusters.hasMoreElements()) { %>
            <% ArrayList<String> cluster = (ArrayList<String>) clusters.nextElement(); %>
            <label> <%= cluster.toString() %> </label> 
            </br>
            <% ArrayList<String> sentences = summarization.get(cluster); %>
            <% for (int i=0; i<sentences.size();++i) { %>
            <label> <%=sentences.get(i).toString() %> </label>
            <% } %>
            </br> </br>
        <% } %>
</div>