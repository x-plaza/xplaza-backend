@extends('layouts.admin_layout')

@section('styles')
    @include('layouts.admin_common_css')

    <link rel="stylesheet" type="text/css" href="{{asset('admin_src/plugins/select2/css/select2.min.css')}}">
    <link rel="stylesheet" type="text/css" href="{{asset('admin_src/plugins/select2-bootstrap4-theme/select2-bootstrap4.min.css')}}">
    <link rel="stylesheet" type="text/css" href="{{asset('admin_src/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css')}}">
@endsection

@section('content')

    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <div class="content-header">
            <div class="container-fluid">
                <div class="row mb-2">
                    <div class="col-sm-6">
                        <h1 class="m-0">Item List</h1>
                    </div><!-- /.col -->
                    <div class="col-sm-6">
                        <ol class="breadcrumb float-sm-right">
                            <li class="breadcrumb-item"><a href="#">Home</a></li>
                            <li class="breadcrumb-item active">Item</li>
                        </ol>
                    </div><!-- /.col -->
                </div><!-- /.row -->
            </div><!-- /.container-fluid -->
        </div>
        <!-- /.content-header -->

        <!-- Main content -->
        <section class="content">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-md-12">
                        <div class="card card-primary card-outline">
                            <div class="card-header">
                                <h3 class="card-title">
                                    <i class="fas fa-add"></i>
                                    Item list
                                </h3>
                            </div>
                            <div class="card-body">

                                <div class="card-body">
                                    <table id="example2" class="table table-bordered table-hover">
                                        <thead>
                                        <tr>
                                            <th>Rendering engine</th>
                                            <th>Browser</th>
                                            <th>Platform(s)</th>
                                            <th>Engine version</th>
                                            <th>CSS grade</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr>
                                            <td>Trident</td>
                                            <td>Internet
                                                Explorer 4.0
                                            </td>
                                            <td>Win 95+</td>
                                            <td> 4</td>
                                            <td>X</td>
                                        </tr>

                                        <tr>
                                            <td>Tasman</td>
                                            <td>Internet Explorer 4.5</td>
                                            <td>Mac OS 8-9</td>
                                            <td>-</td>
                                            <td>X</td>
                                        </tr>
                                        <tr>
                                            <td>Tasman</td>
                                            <td>Internet Explorer 5.1</td>
                                            <td>Mac OS 7.6-9</td>
                                            <td>1</td>
                                            <td>C</td>
                                        </tr>
                                        <tr>
                                            <td>Tasman</td>
                                            <td>Internet Explorer 5.2</td>
                                            <td>Mac OS 8-X</td>
                                            <td>1</td>
                                            <td>C</td>
                                        </tr>
                                        <tr>
                                            <td>Misc</td>
                                            <td>NetFront 3.1</td>
                                            <td>Embedded devices</td>
                                            <td>-</td>
                                            <td>C</td>
                                        </tr>

                                        </tbody>
                                        <tfoot>
                                        <tr>
                                            <th>Rendering engine</th>
                                            <th>Browser</th>
                                            <th>Platform(s)</th>
                                            <th>Engine version</th>
                                            <th>CSS grade</th>
                                        </tr>
                                        </tfoot>
                                    </table>
                                </div>

                            </div>
                            <!-- /.card -->
                        </div>
                    </div>
                </div>
            </div><!-- /.container-fluid -->
        </section>
        <!-- /.content -->
    </div>


@endsection

@section('scripts')
    @include('layouts.admin_common_js')
{{--    <script src="{{asset('admin_src/plugins/jquery-ui/jquery-ui.min.js')}}"></script>--}}
{{--    <script src="{{asset('admin_src/plugins/sparklines/sparkline.js')}}"></script>--}}
{{--    <script src="{{asset('admin_src/plugins/jqvmap/jquery.vmap.min.js')}}"></script>--}}
{{--    <script src="{{asset('admin_src/plugins/jqvmap/maps/jquery.vmap.usa.js')}}"></script>--}}
{{--    <script src="{{asset('admin_src/plugins/jquery-knob/jquery.knob.min.js')}}"></script>--}}
{{--    <script src="{{asset('admin_src/plugins/moment/moment.min.js')}}"></script>--}}
{{--    <script src="{{asset('admin_src/plugins/daterangepicker/daterangepicker.js')}}"></script>--}}
{{--    <script src="{{asset('admin_src/plugins/tempusdominus-bootstrap-4/js/tempusdominus-bootstrap-4.min.js')}}"></script>--}}
{{--    <script src="{{asset('admin_src/plugins/summernote/summernote-bs4.min.js')}}"></script>--}}
{{--    <script src="{{asset('admin_src/plugins/overlayScrollbars/js/jquery.overlayScrollbars.min.js')}}"></script>--}}
{{--    <script src="{{asset('admin_src/dist/js/adminlte.js')}}"></script>--}}
{{--    <script src="{{asset('admin_src/dist/js/demo.js')}}"></script>--}}
{{--    <script src="{{asset('admin_src/dist/js/pages/dashboard.js')}}"></script>--}}

    <script src="{{asset('admin_src/plugins/datatables/jquery.dataTables.min.js')}}"></script>
    <script src="{{asset('admin_src/plugins/bootstrap/js/bootstrap.bundle.min.js')}}"></script>
    <script src="{{asset('admin_src/plugins/datatables-bs4/js/dataTables.bootstrap4.min.js')}}"></script>
    <script>
        $(function () {
            $("#example1").DataTable({
                "responsive": true, "lengthChange": false, "autoWidth": false,
                "buttons": ["copy", "csv", "excel", "pdf", "print", "colvis"]
            });
        })
    </script>
@endsection
