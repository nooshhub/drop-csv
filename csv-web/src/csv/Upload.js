import { Steps, Upload, Icon, message, Table, Button } from 'antd';
import React, { Component } from 'react';
import reqwest from 'reqwest';
import { Link } from 'react-router-dom';

/**
 * 1. upload a csv
 * 2. preview top 10 rows of data
 * 3. choose a line as header
 * 4. click upload and post file and headerLineNum
 * 5. - after uploaded successfully, route a search page 
 * 6. show search url, click search CSV and get data from DB, the table will be with pagination
 */

const { Dragger } = Upload;
const uploadServerHost = 'http://localhost:8080';

const { Step } = Steps;
const steps = [
    {
        title: 'First',
        content: 'Upload csv',
    },
    {
        title: 'Second',
        content: 'Preview before upload, and select one line as header',
    },
    {
        title: 'Last',
        content: 'Upload successfully',
    },
];

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedRowKeys: [0],
            columns: [],
            dataSource: [],
            fileList: [],
            fileName: null,
            uploading: false,
            csvId: null,
            currentStep: 0,
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

                // slice file
                const totalSize = file.size;
                const shardCapacity = 4 * 1024 * 1024; // one shard 4M
                const shardSize = Math.ceil(totalSize / shardCapacity);
                let files = [];
                let sliceStart = 0;
                let sliceEnd = 0;
                for (let i = 0; i < shardSize; i++) {
                    sliceEnd = (i + 1) * shardCapacity;
                    files[i] = file.slice(sliceStart, sliceEnd);
                    sliceStart = sliceEnd;
                }

                console.log(totalSize);
                console.log(shardSize);
                console.log(files);

                this.setState(state => ({
                    columns: columns,
                    dataSource: dataSource,
                    fileName: file.name,
                    fileList: [...state.fileList, ...files],
                    currentStep: state.currentStep + 1,
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

        const { fileList, selectedRowKeys, dataSource, columns, fileName } = this.state;

        const headerSize = columns.length;
        const headers = [];
        for (let i = 0; i < headerSize; i++) {
            headers.push(dataSource[selectedRowKeys[0]]['attr' + i]);
        }

        // 1. init csv table
        reqwest({
            url: uploadServerHost + '/csv/upload/init',
            method: 'post',
            contentType: 'application/json',
            data: JSON.stringify({
                csvFileName: fileName,
                csvHeaders: headers,
            }),
            success: (data) => {

                this.setState(state => ({
                    uploading: false,
                    csvId: data.id,
                }));
                message.success('upload init successfuly');


                // 2. upload files 
                for (let i = 0; i < fileList.length; i++) {
                    const formData = new FormData();
                    formData.append('file', fileList[i]);
                    formData.append('csvShardIndex', i);
                    formData.append('csvId', this.state.csvId);

                    this.setState({
                        uploading: true,
                    })

                    reqwest({
                        url: uploadServerHost + '/csv/upload/multi',
                        method: 'post',
                        processData: false,
                        data: formData,
                        success: (data) => {

                            this.setState(state => ({
                                // fileList: [],
                                uploading: false,
                                // csvId: data.id,
                                // currentStep: state.currentStep + 1,
                            }));
                            message.success('upload successfuly');
                        },
                        error: () => {
                            this.setState({
                                uploading: false,
                            });
                            message.error('upload failed');
                        },
                    });
                }

            },
            error: () => {
                this.setState({
                    uploading: false,
                });
                message.error('upload init failed');
            },
        });




    }


    renderAsStep = () => {
        const currentStep = this.state.currentStep;
        if (currentStep === 0) {

            const { fileList } = this.state;
            const draggerProps = {
                name: 'file',
                multiple: false,
                accept: 'application/vnd.ms-excel',
                beforeUpload: this.beforeUpload,
                fileList,
            }

            return (
                <Dragger {...draggerProps}>
                    <p className="ant-upload-drag-icon">
                        <Icon type="inbox" />
                    </p>
                    <p className="ant-upload-text">Click or drag file to this area to upload</p>
                </Dragger>

            );
        } else if (currentStep === 1) {
            const { selectedRowKeys, dataSource, columns, fileList, uploading } = this.state;

            const rowSelection = {
                selectedRowKeys,
                onChange: this.onSelectChange,
            }

            return (
                <div>
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
                </div>
            );
        } else if (currentStep === 2) {
            const { csvId } = this.state;
            return (
                <div>
                    <Link to={`/csv/edit/${csvId}`}>View Uploaded CSV</Link>
                </div>
            );
        }
    }

    render() {

        const { currentStep } = this.state;

        return (
            <div className="container">

                <Steps current={currentStep}>
                    {steps.map(item => (
                        <Step key={item.title} title={item.title} />
                    ))}
                </Steps>
                <div className="steps-content">{steps[currentStep].content}</div>

                {this.renderAsStep(currentStep)}

            </div>
        );
    }
}

export default App;

