<#-- @ftlvariable name="description" type="java.lang.String" -->
<#-- @ftlvariable name="location" type="java.lang.String" -->
<#-- @ftlvariable name="time" type="java.lang.String" -->
<#-- @ftlvariable name="id" type="java.lang.String" -->
<#-- @ftlvariable name="errorMsg" type="java.lang.String" -->


<!doctype html public "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Fly me to Mars: a mission registration system.</title>

    <meta http-equiv="Content-type" content="text/html;charset=UTF-8">

    <meta name="description" content="Fly me to Mars: update mission.">
</head>

<body>

<div id="title_pane">
    <h3>Mission Update</h3>
</div>

<p>${errorMsg!""}</p>

<div>
    <p>* Fields are required.</p>
</div>
<form name="update_event" action="/mission/${id}/update" method="POST">
    <div id="admin_left_pane" class="fieldset_without_border">
        <div><p>Mission Details</p></div>
        <ol>
            <li>
                <label for="time" class="bold">Date and time (dd/mm/yyyy, HH AM/PM):*</label>
                <input id="time" name="time" type="text" value="${time!""}">
            </li>
            <li>
                <label for="location" class="bold">Location:*</label>
                <input id="location" name="location" type="text" value="${location!""}">
            </li>
            <li>
                <label for="description" class="bold">Description:</label>
                <input id="description" name="description" type="text" value="${description!""}">
            </li>
        </ol>
    </div>

<#if errorMsg?? && errorMsg?has_content>
    <div id="error">
        <p>Error: ${errorMsg}</p>
    </div>
</#if>
    <div id="buttonwrapper">
        <button type="submit">Update Mission</button>
        <a href="/">Cancel</a>
    </div>
</form>

</body>