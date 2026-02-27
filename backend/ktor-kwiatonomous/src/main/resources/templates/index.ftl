<#-- @ftlvariable name="devices" type="kotlin.collections.List<com.corrot.db.data.model.Device>" -->
<#setting time_zone="GMT">
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<style>
  body {
    font-family: system-ui, -apple-system, Segoe UI, Roboto, sans-serif;
    background: #f5f7fa;
    padding: 24px;
  }

  .table-wrapper {
    max-width: 700px;
    margin: auto;
  }

  table {
    width: 100%;
    border-collapse: collapse;
    background: white;
    border-radius: 12px;
    overflow: hidden;
    box-shadow: 0 6px 18px rgba(0,0,0,0.08);
  }

  thead {
    background: #f0f2f5;
  }

  th, td {
    padding: 14px 16px;
    text-align: center;
  }

  th {
    font-weight: 600;
    font-size: 14px;
    color: #333;
  }

  tbody tr {
    border-top: 1px solid #eee;
  }

  tbody tr:hover {
    background: #fafbfc;
  }

  .status {
    font-size: 18px;
  }

  .footer {
    text-align: right;
    font-size: 12px;
    color: #888;
    margin-top: 6px;
  }
</style>
</head>

<body>
<div class="table-wrapper">
  <table>
    <thead>
      <tr>
        <th>Active</th>
        <th>Device ID</th>
        <th>Last updated</th>
      </tr>
    </thead>
    <tbody>
      <#list devices?reverse as device>
        <tr>
          <td class="status">
            <#if (((((.now?long / 1000.0) - device.lastUpdate) / 60.0)) <= 30.0)>
              🟢
            <#else>
              🔴
            </#if>
          </td>
          <td>${device.deviceId}</td>
          <td>${((device.lastUpdate*1000))?number_to_datetime}</td>
        </tr>
      </#list>
    </tbody>
  </table>

  <div class="footer">
    Last refresh: ${.now?string.medium}
  </div>
</div>
</body>
</html>
