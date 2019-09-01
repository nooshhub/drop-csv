
import { Upload, Icon, message, Table } from 'antd';

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

function beforeUpload(file) {
  const isCsv = file.type === 'application/vnd.ms-excel';
  if (!isCsv) {
    message.error('You can only upload csv file!');
  }
  // const isLt2M = file.size / 1024 / 1024 < 2;
  // if (!isLt2M) {
  //   message.error('Image must smaller than 2MB!');
  // }
  // return isCsv && isLt2M;

  // TODO:
  // if we not limit here, and show the top 10 rows of data,
  // how do we do that

  console.log(file);
  if (typeof (FileReader) !== 'undefined') {    //H5
    var reader = new FileReader();
    reader.readAsText(file);            //以文本格式读取
    // reader.readAsArrayBuffer(file);            //以文本格式读取
    
    // TODO: limit the load progress at 1000 bytes

    reader.onload = function (evt) {
      var data = evt.target.result;        //读到的数据
      var lineBreakIndex = data.indexOf("\n");
      console.log(data.substr(0, lineBreakIndex));
      // Get Line breaker and read line by line, until we hit the end
      // and only show the first 4, right.

      for (let i = 0; i < 3; i++) {
        console.log(i);
      }

      console.log(data);
      // console.log(data.byteLength);
      // var view   = new DataView(data, 0, 200);
      // console.log(view.byteLength);
    }
  } else {
    message.error("IE9及以下浏览器不支持，请使用Chrome或Firefox浏览器");
  }

  return isCsv;
}

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


export default function () {
  return (
    <div>
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


