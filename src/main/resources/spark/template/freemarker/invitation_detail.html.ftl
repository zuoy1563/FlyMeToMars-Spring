<#-- @ftlvariable name="errorMsg" type="java.lang.String" -->
<#-- @ftlvariable name="invitation" type="flymetomars.model.Invitation" -->

<!doctype html public "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Fly me to Mars: a mission registration system.</title>

    <meta http-equiv="Content-type" content="text/html;charset=UTF-8">

    <meta name="description" content="Fly me to Mars - a mission registration system.">
</head>

<body>
<div id="title_pane">
    <h3>Invitation Details Page</h3>
</div>

<div>
<#if errorMsg?? && errorMsg?has_content>
    <li><h4 class="errorMsg">${errorMsg}</h4></li>
<#else>
    <p>Invitation details:</p>
    <ul>
        <li>Creator: ${invitation.creator.firstName} ${invitation.creator.lastName}</li>
        <li>Recipient: ${invitation.recipient.firstName} ${invitation.recipient.lastName}</li>
        <li>Last Updated Time: ${invitation.lastUpdated?datetime}</li>
        <li>Mission: <a href="/mission/${invitation.mission.id}">${invitation.mission.name}</a></li>
        <li>Invitation Status: ${invitation.status.name()}</li>
        <p></p>
        <p><a href="/person/${invitation.creator.id}">Back to my personal page</a></p>
    </ul>
</#if>

</div>

</body>
</html>