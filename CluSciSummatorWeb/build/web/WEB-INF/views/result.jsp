<%-- 
    Document   : index
    Created on : 15 Apr 12, 09:01:26
    Author     : Akbar Gumbira (akbargumbira@gmail.com)
--%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Hashtable" %>


<% Hashtable<ArrayList<String>, ArrayList<String>> summarization = (Hashtable<ArrayList<String>, ArrayList<String>>) request.getAttribute("summarization");%>
<% String tes = (String) request.getAttribute("tes");%>

<div class="left_column">
    <label class="font2">Cluster Description: </label>
    </br>
    <% Enumeration clusters = summarization.keys();%>
        <% while (clusters.hasMoreElements()) {%>
                <% ArrayList<String> cluster = (ArrayList<String>) clusters.nextElement();%>
                <label class="font1"> <%= cluster.toString()%> </label> 
                </br>
        <% }%>
</div>

<div class="right_column">
    <div class="summarization_result" id="result">
        <% Enumeration clusters2 = summarization.keys();%>
        <% while (clusters2.hasMoreElements()) {%>
            <div class="box">
                <% ArrayList<String> cluster = (ArrayList<String>) clusters2.nextElement();%>
                <label class="font1"> <%= cluster.toString()%> </label> 
                </br>
                <% ArrayList<String> sentences = summarization.get(cluster);%>
                <% for (int i = 0; i < sentences.size(); ++i) {%>
                <label> <%=sentences.get(i).toString()%> </label>
                <% }%>
                </br> </br>
            </div>
        <% }%>
    </div>
</div>
    
<div class="clearboth"></div>