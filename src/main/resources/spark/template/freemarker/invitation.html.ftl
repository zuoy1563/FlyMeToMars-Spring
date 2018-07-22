<#-- @ftlvariable name="recipient" type="java.lang.String" -->
<#-- @ftlvariable name="mission" type="flymetomars.model.Mission" -->
<#-- @ftlvariable name="errorMsg" type="java.lang.String" -->

<!doctype html public "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Fly me to Mars - a mission registration system.</title>

    <meta http-equiv="Content-type" content="text/html;charset=UTF-8">

    <meta name="description" content="Fly me to Mars - invitation creator.">
</head>

<body>

<div id="title_pane">
    <h3>Invitation creation</h3>
</div>

<div>
    <p>* Fields are required.</p>
</div>
<form name="create_invitation" action="/mission/${mission.id}/create_invitation" method="POST">
    <div id="admin_left_pane" class="fieldset_without_border">
        <div class="legend_no_indent">Invitation Details</div>
        <ol>
            <li>
                <label for="recipient" class="bold">Recipient email:*</label>
                <input id="recipient" name="recipient" type="text" value="${recipient}">
            </li>
        </ol>
    </div>

<#if errorMsg?? && errorMsg?has_content>
    <div id="error">
        <p>Error: ${errorMsg}</p>
    </div>
</#if>

    <div id="buttonwrapper">
        <button type="submit">Create Invitation</button>
        <a href="/">Cancel</a>
    </div>
</form>
</body>
</html>