<%-- 
    Document   : index
    Created on : 15 Apr 12, 09:01:26
    Author     : Akbar Gumbira (akbargumbira@gmail.com)
--%>
<%@page import="core.utility.Global"%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Hashtable" %>

<div class="one_column">
    <div class="input_summarize">
        <table>
            <tr>
                <td> <label>Kategori Retorik</label> </td>
                <td> <select name="kategori_retorik" id="kategori_retorik">
                        <% for (int i = 0; i < Global.rhetoricalStatusList.size(); i++) {%>
                        <option value="<%= Global.rhetoricalStatusList.get(i)%>"> <%=Global.rhetoricalStatusList.get(i)%></option>
                        <% }%>
                    </select>
                </td>
            </tr>
            <tr>
                <td><label>Minimum Support</label></td>
                <td><input type="text" name="minimum_support" id="minimum_support" value="0.7"/></td>
            </tr>
            <tr>
                <td><button id="summarize"/>Summarize!</td>
                <td><div id="loading">        
                    </div>
                </td>

            </tr>
        </table>
        <div id="informasi_retorik"></div> 
    </div>
</div>

<div id="ajax_result">
    <div class="left_column">
        <label class="font2">Topics: </label>
        </br>
        <div id="topics">
        </div>
    </div>

    <div class="right_column">
        <label class="font2">Summary: </label>
        </br>
        <div class="summarization_result" id="result"> 
        </div>
    </div>
</div>
<div class="clearboth"></div>

<script>
    function printSummary(summaryCluster) {
        var result = "</br>";
        
        jQuery.each(summaryCluster, function(){
            var detailOfSentence = this;
            var i = 0;
            jQuery.each(detailOfSentence, function(){
                if (i==0)
                    result += "<label class='font3'>"+this+" </label>";
                else if (i==1)
                    result += "<a href='data/paper/"+this+"'><label class='font3'>See Section</label></a></br>";
                else
                    result += this+"</br>";             
                ++i;
            });
            result += "</br>";              
        });
        
        return result;
    };
    
    $(document).ready(function() {  
        $.ajaxSetup ({
            cache: false
        });
        var ajax_load = "<img class='loading' id='loading' src='data/img/ajax-loader.gif' alt='loading...' />";
        $("#summarize").click(function() {
            var  kategori_retorik = $("#kategori_retorik").val();
            var  minimum_support = $("#minimum_support").val();
            
            if ( minimum_support.length == 0) {
                $("#minimum_support").focus();
            } else {
                $('#topics').children().remove();
                $('#result').children().remove();
                $("#loading").html(ajax_load);
                $.post(
                "result", //URL
                {
                    kategori_retorik : ""+kategori_retorik,
                    minimum_support : ""+minimum_support

                }, //DATA
                function(data) {
                    $("#loading").children().remove();
                    $.each(data, function(key, value) {
                        $("#topics").append('<label class="font1">'+key+'</label></br>');
                        if (key=="")
                            $("#result").append('<div class="transparent_box"><label class="font1_bold">'+key+'</label></br>'+ value +'</br></div>');
                        else
                            $("#result").append('<div class="transparent_box"><label class="font1_bold">'+key+'</label></br>'+ printSummary(value) +'</br></div>');
                    });
                        
                }
            )
            }
        });
        
         $("select[name='kategori_retorik']").change(function () {
            var selectedRetorik = $("select[name='kategori_retorik'] option:selected").val();
             $.post(
                "result", //URL
                {
                    kategori_retorik : ""+selectedRetorik,
                    informasi_retorik : "true"
                }, //DATA
                function(data) {
                    $("#informasi_retorik").html(data);
                }
            )
        })
        .trigger('change');

    });
</script>
