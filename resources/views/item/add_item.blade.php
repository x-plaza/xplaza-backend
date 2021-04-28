@extends('layouts.admin_layout')

@section('styles')
    @include('layouts.admin_common_css')

    <!-- Ionicons -->
{{--    <link rel="stylesheet" href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css">--}}
{{--    <link rel="stylesheet" type="text/css" href="{{asset('admin_src/plugins/tempusdominus-bootstrap-4/css/tempusdominus-bootstrap-4.min.css')}}">--}}
{{--    <link rel="stylesheet" type="text/css" href="{{asset('admin_src/plugins/icheck-bootstrap/icheck-bootstrap.min.css')}}">--}}
{{--    <link rel="stylesheet" type="text/css" href="{{asset('admin_src/plugins/jqvmap/jqvmap.min.css')}}">--}}
{{--    <link rel="stylesheet" type="text/css" href="{{asset('admin_src/plugins/overlayScrollbars/css/OverlayScrollbars.min.css')}}">--}}
{{--    <link rel="stylesheet" type="text/css" href="{{asset('admin_src/plugins/daterangepicker/daterangepicker.css')}}">--}}
{{--    <link rel="stylesheet" type="text/css" href="{{asset('admin_src/plugins/summernote/summernote-bs4.min.css')}}">--}}
    <!-- Select2 -->

    <link rel="stylesheet" type="text/css" href="{{asset('admin_src/plugins/select2/css/select2.min.css')}}">
    <link rel="stylesheet" type="text/css" href="{{asset('admin_src/plugins/select2-bootstrap4-theme/select2-bootstrap4.min.css')}}">
@endsection

@section('content')

    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <div class="content-header">
            <div class="container-fluid">
                <div class="row mb-2">
                    <div class="col-sm-6">
                        <h1 class="m-0">New Item</h1>
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
                                    Add new Item
                                </h3>
                            </div>
                            <div class="card-body">
                                <h4></h4>
                                <div class="row">
                                    <div class="col-5 col-sm-3">
                                        <div class="nav flex-column nav-tabs h-100" id="vert-tabs-tab" role="tablist" aria-orientation="vertical">
                                            <a class="nav-link active" id="vert-tabs-home-tab" data-toggle="pill" href="#vert-tabs-home" role="tab" aria-controls="vert-tabs-home" aria-selected="true">Details</a>
                                            <a class="nav-link" id="vert-tabs-messages-tab" data-toggle="pill" href="#vert-tabs-messages" role="tab" aria-controls="vert-tabs-messages" aria-selected="false">Shop</a>
                                            <a class="nav-link" id="vert-tabs-settings-tab" data-toggle="pill" href="#vert-tabs-settings" role="tab" aria-controls="vert-tabs-settings" aria-selected="false">Media</a>
                                        </div>
                                    </div>
                                    <div class="col-7 col-sm-9">
                                        <div class="tab-content" id="vert-tabs-tabContent">
                                            <div class="tab-pane text-left fade show active" id="vert-tabs-home" role="tabpanel" aria-labelledby="vert-tabs-home-tab">

                                                <div class="card card-primary">
                                                    <div class="card-header">
                                                        <h3 class="card-title">Item details</h3>
                                                    </div>
                                                        <div class="card-body">
                                                            <div class="form-group">
                                                                <label for="exampleInputEmail1">Email address</label>
                                                                <input type="email" class="form-control" id="exampleInputEmail1" placeholder="Enter email">
                                                            </div>
                                                            <div class="form-group">
                                                                <label for="exampleInputPassword1">Password</label>
                                                                <input type="password" class="form-control" id="exampleInputPassword1" placeholder="Password">
                                                            </div>
                                                            <div class="form-group">
                                                                <label for="exampleInputFile">File input</label>
                                                                <div class="input-group">
                                                                    <div class="custom-file">
                                                                        <input type="file" class="custom-file-input" id="exampleInputFile">
                                                                        <label class="custom-file-label" for="exampleInputFile">Choose file</label>
                                                                    </div>
                                                                    <div class="input-group-append">
                                                                        <span class="input-group-text">Upload</span>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div class="form-check">
                                                                <input type="checkbox" class="form-check-input" id="exampleCheck1">
                                                                <label class="form-check-label" for="exampleCheck1">Check me out</label>
                                                            </div>
                                                        </div>
                                                </div>

                                            </div>

                                            <div class="tab-pane fade" id="vert-tabs-messages" role="tabpanel" aria-labelledby="vert-tabs-messages-tab">

                                                <div class="card card-primary">
                                                    <div class="card-header">
                                                        <h3 class="card-title">Select in available shop</h3>
                                                    </div>
                                                    <div class="card-body">
                                                        <div class="">
                                                            <div class="form-group">
                                                                <label>Shop name</label>
                                                                <select class="select2" multiple="multiple" data-placeholder="Select a State" style="width: 100%;">
                                                                    <option>Alabama</option>
                                                                    <option>Alaska</option>
                                                                    <option>California</option>
                                                                    <option>Delaware</option>
                                                                    <option>Tennessee</option>
                                                                    <option>Texas</option>
                                                                    <option>Washington</option>
                                                                </select>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                            </div>
                                            <div class="tab-pane fade" id="vert-tabs-settings" role="tabpanel" aria-labelledby="vert-tabs-settings-tab">

                                                <div class="card card-primary">
                                                    <div class="card-header">
                                                        <h3 class="card-title">Media</h3>
                                                    </div>
                                                    <div class="card-body">
                                                        <div class="form-group">
                                                            <label for="exampleInputFile">File one</label>
                                                            <div class="input-group">
                                                                <div class="custom-file">
                                                                    <input type="file" class="custom-file-input" id="exampleInputFile">
                                                                    <label class="custom-file-label" for="exampleInputFile">Choose file</label>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label for="exampleInputFile">File two</label>
                                                            <div class="input-group">
                                                                <div class="custom-file">
                                                                    <input type="file" class="custom-file-input" id="exampleInputFile">
                                                                    <label class="custom-file-label" for="exampleInputFile">Choose file</label>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                            </div>
                                        </div>
                                    </div>

                                    <div class="card-footer">
                                        <button type="submit" class="btn btn-primary">Save Info</button>
                                    </div>
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

    <script src="{{asset('admin_src/plugins/select2/js/select2.full.min.js')}}"></script>
    <script>
        $(function () {
            //Initialize Select2 Elements
            $('.select2').select2()

            //Initialize Select2 Elements
            $('.select2bs4').select2({
                theme: 'bootstrap4'
            })
        })
    </script>
@endsection
