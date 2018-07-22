<#-- @ftlvariable name="errorMsg" type="java.lang.String" -->
<#-- @ftlvariable name="user" type="flymetomars.model.Person" -->
<#-- @ftlvariable name="person" type="flymetomars.model.Person" -->
<#-- @ftlvariable name="invitationsReceived" type="java.util.Set<flymetomars.model.Invitation>" -->
<#-- @ftlvariable name="invitationsSent" type="java.util.Set<flymetomars.model.Invitation>" -->
<#-- @ftlvariable name="missions" type="java.util.Set<flymetomars.model.Mission>" -->

<!doctype html public "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Fly me to Mars: a mission registration system.</title>

    <meta http-equiv="Content-type" content="text/html;charset=UTF-8">

    <meta name="description" content="Fly me to Mars - a mission registration system.">
</head>

<body>
<div id="title_pane">
<#if person??>
    <h3>${person.firstName} ${person.lastName}'s Page</h3>
<#else>
    <h3>Person page</h3>
    <p>Error: ${errorMsg}</p>
</#if>
</div>

<div>

<#if user??>
    <span><h3>Welcome back: ${user.firstName} ${user.lastName}!</h3></span>
    <p><a href="/logout">Logout</a></p>
<#else>
    <span><h3>Welcome to Fly Me To Mars!</h3></span>
    <p>You can <a href="/login">Login</a> or <a href="/register">register</a>.</p>
</#if>
<#if person??>
    <#if invitationsReceived?? && invitationsReceived?has_content>
        <p>Invitations Received.</p>
        <ul>
            <#list invitationsReceived as invitationReceived>
                <li>${invitationReceived.mission.name}, ${invitationReceived.mission.time?datetime}, ${invitationReceived.status.name()}</li>
            </#list>
        </ul>
    <#else>
        <p>It appears there isn't any invitation received yet.</p>
    </#if>

    <#if invitationsSent?? && invitationsSent?has_content>
        <p>Invitations Sent.</p>
        <ul>
            <#list invitationsSent as invitationSent>
                <li>${invitationSent.mission.name}, ${invitationSent.mission.time?datetime}, ${invitationSent.status.name()}</li>
            </#list>
        </ul>
    <#else>
        <p>It appears there isn't any invitation sent yet.</p>
    </#if>

    <#if missions?? && missions?has_content>
        <p>All missions attended.</p>
        <ul>
            <#list missions as mission>
                <li><a href="/mission/${mission.id}">${mission.name}</a>, with captain ${mission.captain.firstName} ${mission.captain.lastName}, at ${mission.time?datetime}, ${mission.location}.</li>
            </#list>
        </ul>
    <#else>
        <p>It appears there isn't any mission yet.</p>
    </#if>
    <p><a href="/hello">Back to home page</a></p>
</#if>
</div>

</body>
</html>