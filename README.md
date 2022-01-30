# ir-data-compiler
This program is used to combine data point table (`.dpt`) files exported from Bruker OPUS spectroscopy software into one compiled file containing all of the data.

For example, these three files:
<table>
<thead>
  <tr>
    <th>File A</th>
    <th>File B</th>
    <th>File C</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td>1001.75537,-0.00016<br>1001.24165,-0.00009<br>1000.72793,-0.00002<br>1000.21421,0.00007<br>...</td>
    <td>1001.75537,0.00981<br>1001.24165,0.00402<br>1000.72793,0.00191<br>1000.21421,0.00180<br>...</td>
    <td>1001.75537,0.01850<br>1001.24165,0.00742<br>1000.72793,0.00335<br>1000.21421,0.00329<br>...</td>
  </tr>
</tbody>
</table>
Will be converted into:
<table>
<thead>
  <tr>
    <th>Compiled Data File</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td>1001.75537,-0.00016,0.00981,0.01850<br>1001.24165,-0.00009,0.00402,0.00742<br>1000.72793,-0.00002,0.00191,0.00335<br>1000.21421,0.00007,0.00180,0.00329<br>...</td>
  </tr>
</tbody>
</table>
