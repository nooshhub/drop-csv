import React, { Component } from 'react';

import { message, Table, Button } from 'antd';
import reqwest from 'reqwest';

const uploadServerHost = 'http://localhost:8080';

const columns = [
    {
        title: "Id",
        dataIndex: "id",
    },
    {
        title: "CSV Name",
        dataIndex: "csvName",
    },
    {
        title: "Search Name",
        dataIndex: "searchName",
    },
];

class ListCsvView extends Component {
    constructor(props) {
        super(props);
        console.log(props)
        this.state = {
            selectedRowKeys: [0],
            columns: columns,
            dataSource: [],
        };
    }

    componentDidMount() {
        reqwest({
            url: uploadServerHost + '/csv/list/',
            success: (data) => {
                this.setState({ dataSource: data });
                message.success('find csv list');
            },
            error: () => {
                message.error('Csv not exist');
            }
        })
    }

    deleteSelectedRows = () => {
        const { selectedRowKeys } = this.state;

        message.success(selectedRowKeys + ' were deleted');
    }

    onSelectChange = selectedRowKeys => {
        this.setState({ selectedRowKeys: selectedRowKeys });
    };

    render() {
        const { selectedRowKeys, dataSource, columns } = this.state;

        const rowSelection = {
            selectedRowKeys,
            onChange: this.onSelectChange,
        }

        return (
            <div className='container'>
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

export default ListCsvView;