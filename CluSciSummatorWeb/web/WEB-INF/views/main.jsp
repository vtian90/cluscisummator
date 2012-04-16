<%-- 
    Document   : index
    Created on : 14 Apr 12, 23:04:17
    Author     : Akbar Gumbira (akbargumbira@gmail.com)
--%>

<%@page import="core.utility.Global" %>

<div class="input_summarize" id="input_summarize">
    <form method="post" action="<%=request.getContextPath()%>/result">
        <table>
            <tr>
                <td> <label>Kategori Retorik</label> </td>
                <td> <select name="kategori_retorik">
                        <% for (int i = 0; i < Global.rhetoricalStatusList.size(); i++) {%>
                        <option value="<%= Global.rhetoricalStatusList.get(i)%>"> <%=Global.rhetoricalStatusList.get(i)%></option>
                        <% }%>
                    </select>
                </td>
            </tr>
            <tr>
                <td><label>Minimum Support</label></td>
                <td><input type="text" name="minimum_support"/></td>
            </tr>
            <tr>
                <td><input type="submit" value="Summarize!" /></td>
            </tr>
        </table>
    </form>
</div>