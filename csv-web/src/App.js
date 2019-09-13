
import { Upload, Icon, message, Table, Button } from 'antd';
import React, { Component } from 'react';
import reqwest from 'reqwest';

/**
 * 1. upload a csv
 * 2. preview top 10 rows of data
 * 3. choose a line as header
 * 4. - click upload and post file and headerLineNum
 * 5. after uploaded successfully, click test and get data from DB, the table will be with pagination
 */

const { Dragger } = Upload;

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      selectedRowKeys: [0],
      columns: [],
      dataSource: [],
      fileList: [],
      uploading: false,
      testLink: null,
    };
    this.beforeUpload = this.beforeUpload.bind(this); // properly bound once
  }


  /**
   * only select the lastest selected one
   */
  onSelectChange = selectedRowKeys => {
    let newSelectedKeys = [selectedRowKeys[1]];
    this.setState({ selectedRowKeys: newSelectedKeys });
  };

  /**
   * Before Upload, preivew top 10 data, and select header line
   * @param {*} file 
   */
  beforeUpload = (file) => {
    const isCsv = file.type === 'application/vnd.ms-excel';
    if (!isCsv) {
      message.error('You can only upload csv file!');
    }

    if (typeof (FileReader) !== 'undefined') {    //H5
      var reader = new FileReader();

      reader.onload = (evt) => {
        var data = evt.target.result;

        // lines
        var lines = data.split('\n');

        // get the first line, and preview top 10 data
        const columns = [];
        const dataSource = [];
        const columnCount = lines[0].split(",").length;
        for (let columnIndex = 0; columnIndex < columnCount; columnIndex++) {
          columns.push(
            {
              title: 'column ' + columnIndex,
              dataIndex: 'attr' + columnIndex,
            },
          )
        }

        for (let rowIndex = 0; rowIndex < 10; rowIndex++) {
          let line = {};
          line['key'] = rowIndex;

          let dataLineArr = lines[rowIndex].split(",");
          for (let columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            line['attr' + columnIndex] = dataLineArr[columnIndex];
          }

          dataSource.push(line);
        }

        this.setState(state => ({
          columns: columns,
          dataSource: dataSource,
          fileList: [...state.fileList, file],
        }));

      }

      // limit file to 1024 for preview
      reader.readAsText(file.slice(0, 1024));

    } else {
      message.error("IE9 is not support, please use Chrome or Firefox");
    }

    return false;
  }

  handleUpload = () => {
    const { fileList, selectedRowKeys } = this.state;
    const formData = new FormData();

    formData.append('file', fileList[0]);
    formData.append('headerLineNum', selectedRowKeys[0]);

    this.setState({
      uploading: true,
    })

    const uploadServerHost = 'http://localhost:8080';
    reqwest({
      url: uploadServerHost + '/csv/upload',
      method: 'post',
      processData: false,
      data: formData,
      success: (data) => {

        this.setState({
          fileList: [],
          uploading: false,
          testLink: uploadServerHost + data.searchUrl,
        });
        message.success('upload successfuly');
      },
      error: () => {
        this.setState({
          uploading: false,
        });
        message.success('upload failed');
      },
    });
  }

  render() {

    const { selectedRowKeys, dataSource, columns } = this.state;
    const { fileList, uploading, testLink } = this.state;

    const rowSelection = {
      selectedRowKeys,
      onChange: this.onSelectChange,
    }

    const draggerProps = {
      name: 'file',
      multiple: false,
      accept: 'application/vnd.ms-excel',
      beforeUpload: this.beforeUpload,
      fileList,
    }

    return (
      <div className="container">

        <Dragger {...draggerProps}>
          <p className="ant-upload-drag-icon">
            <Icon type="inbox" />
          </p>
          <p className="ant-upload-text">Click or drag file to this area to upload</p>
          <p className="ant-upload-hint">
            Support for a single or bulk upload. Strictly prohibit from uploading company data or other
            band files
          </p>
        </Dragger>

        <Table rowSelection={rowSelection}
          dataSource={dataSource}
          columns={columns} />

        <Button
          type="primary"
          onClick={this.handleUpload}
          disabled={fileList.length === 0}
          loading={uploading}
          style={{ marginTop: 16 }}
        >
          {uploading ? 'Uploading' : 'Start Upload'}
        </Button>
        <Button
          type="primary"
          disabled={!testLink}
          style={{ marginTop: 16, marginLeft: 10 }}
        >Test</Button>
      </div>
    );
  }
}

export default App;

