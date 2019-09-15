import React, { Component } from 'react';

import { message, Table, Button } from 'antd';
import reqwest from 'reqwest';

const uploadServerHost = 'http://localhost:8080';

class EditCsvView extends Component {
    constructor(props) {
        super(props);
        console.log(props)
        this.state = {
            selectedRowKeys: [0],
            columns: [],
            dataSource: [],
            csvInfo: {},
        };
    }

    componentDidMount() {
        console.log(this.props)
        const id = this.props.match.params.id;

        reqwest({
            url: uploadServerHost + '/csv/info/' + id,
            success: (data) => {
                this.setState({ csvInfo: data });

                message.success('find csv, search csv data');

                const searchLink = '/csv/_search/' + data.searchName;
                this.searchCsv(searchLink);
            },
            error: () => {
                message.error('Csv not exist');
            }
        })
    }

    searchCsv = (searchLink) => {
        reqwest({
            url: uploadServerHost + searchLink,
            success: (data) => {
                this.setState({
                    columns: data.columns,
                    dataSource: data.dataSource,
                });
                message.success('search successfuly, refresh table');
            },
            error: () => {
                message.error('search failed');
            }
        });
    }

    deleteSelectedRows = () => {
        const { selectedRowKeys } = this.state;

        message.success(selectedRowKeys + ' were deleted');
    }

    onSelectChange = selectedRowKeys => {
        this.setState({ selectedRowKeys: selectedRowKeys });
    };

    render() {
        const { selectedRowKeys, dataSource, columns, csvInfo } = this.state;

        const rowSelection = {
            selectedRowKeys,
            onChange: this.onSelectChange,
        }

        return (
            <div className='container'>
                <div>
                    <p><b>CSV Info</b></p>
                    <p>{csvInfo.id}</p>
                    <p>{csvInfo.csvName}</p>
                    <p>{csvInfo.searchName}</p>
                </div>

                <Table rowSelection={rowSelection}
                    dataSource={dataSource}
                    columns={columns} />

                <Button
                    type="danger"
                    disabled={selectedRowKeys.length === 0}
                    style={{ marginTop: 16, marginLeft: 10 }}
                    onClick={this.deleteSelectedRows}
                >Delete</Button>

            </div>
        );
    };

}

export default EditCsvView;