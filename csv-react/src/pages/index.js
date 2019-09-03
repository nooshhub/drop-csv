
import { Upload, Icon, message, Table } from 'antd';
import Link from 'umi/link';

const { Dragger } = Upload;

const props = {
  name: 'file',
  multiple: false,
  accept: 'application/vnd.ms-excel',
  action: 'https://www.mocky.io/v2/5cc8019d300000980a055e76',
  onChange(info) {
    const { status } = info.file;
    if (status !== 'uploading') {
      console.log(info.file, info.fileList);
    }
    if (status === 'done') {
      message.success(`${info.file.name} file uploaded successfully.`);
    } else if (status === 'error') {
      message.error(`${info.file.name} file upload failed.`);
    }
  },
  beforeUpload: beforeUpload
};

const dataSource = [
  {
    key: '1',
    name: '胡彦斌',
    age: 32,
    address: '西湖区湖底公园1号',
  },
  {
    key: '2',
    name: '胡彦祖',
    age: 42,
    address: '西湖区湖底公园1号',
  },
];

const columns = [
  {
    title: '姓名',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: '年龄',
    dataIndex: 'age',
    key: 'age',
  },
  {
    title: '住址',
    dataIndex: 'address',
    key: 'address',
  },
];

const headerPosition = 0;

function beforeUpload(file) {
  const isCsv = file.type === 'application/vnd.ms-excel';
  if (!isCsv) {
    message.error('You can only upload csv file!');
  }

  if (typeof (FileReader) !== 'undefined') {    //H5
    var reader = new FileReader();

    reader.onload = function (evt) {
      var data = evt.target.result;
      // By lines
      var lines = this.result.split('\n');

      let headerLine = lines[headerPosition];
      const headers = headerLine.split(",");
      for (let [i, header] of headers.entries()) {
        columns.push(
          {
            title: header,
            dataIndex: i,
            key: header,
          }
        );
      }
      console.log(columns);

      const limitedLinesCount = lines.length - 1;
      const lineDataStartIndex = headerPosition + 1;
      for (var dataLineIndex = lineDataStartIndex; dataLineIndex < limitedLinesCount; dataLineIndex++) {
        const dataLine = lines[dataLineIndex];
        const dataArr = dataLine.split(",");
        let data = {
          key: dataLineIndex,
        }

        for (let [i, header] of headers.entries()) {
          data[i] = dataArr[i];
        }

        dataSource.push(data);
      }
      console.log(dataSource);

      // TODO: now we have the columns, datasource, how do we render the table with them
      // this time you will need to get more familiar with umi and dva

    }

    // limit file to 1024 for preview
    reader.readAsText(file.slice(0, 1024));

  } else {
    message.error("IE9及以下浏览器不支持，请使用Chrome或Firefox浏览器");
  }

  return isCsv;
}



export default function () {
  return (
    <div className="container">
      <div className="menu">
        <Link to="products">Products</Link>
      </div>
      <Dragger {...props}>
        <p className="ant-upload-drag-icon">
          <Icon type="inbox" />
        </p>
        <p className="ant-upload-text">Click or drag file to this area to upload</p>
        <p className="ant-upload-hint">
          Support for a single or bulk upload. Strictly prohibit from uploading company data or other
          band files
      </p>
      </Dragger>
      <Table dataSource={dataSource} columns={columns} />;
    </div>
  );
}


