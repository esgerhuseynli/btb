import 'package:get_it/get_it.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:device_info_plus/device_info_plus.dart';
import 'package:local_auth/local_auth.dart';

import '../core/network/dio_client.dart';
import '../data/datasources/remote/api_service.dart';
import '../data/repositories/auth_repository.dart';
import '../data/repositories/bank_accounts_repository.dart';
import '../data/services/sima_service.dart';
import '../core/utils/request_builder.dart';
import '../presentation/auth/bloc/auth_bloc.dart';
import '../presentation/home/bloc/home_bloc.dart';

final getIt = GetIt.instance;

Future<void> configureDependencies() async {
  // Register core dependencies
  getIt.registerLazySingleton<FlutterSecureStorage>(
    () => const FlutterSecureStorage(),
  );
  getIt.registerLazySingleton<DeviceInfoPlugin>(
    () => DeviceInfoPlugin(),
  );

  // Register DioClient (depends on FlutterSecureStorage)
  getIt.registerLazySingleton<DioClient>(
    () => DioClient(getIt<FlutterSecureStorage>()),
  );

  // Register API Service (depends on DioClient)
  getIt.registerLazySingleton<ApiService>(
    () => ApiService(getIt<DioClient>().dio),
  );

  // Register RequestBuilder (depends on FlutterSecureStorage and DeviceInfoPlugin)
  getIt.registerLazySingleton<RequestBuilder>(
    () => RequestBuilder(
      getIt<FlutterSecureStorage>(),
      getIt<DeviceInfoPlugin>(),
    ),
  );

  // Register Repositories (depend on ApiService)
  getIt.registerLazySingleton<AuthRepository>(
    () => AuthRepository(getIt<ApiService>()),
  );
  getIt.registerLazySingleton<BankAccountsRepository>(
    () => BankAccountsRepository(getIt<ApiService>()),
  );

  // Register Services
  getIt.registerLazySingleton<SimaService>(
    () => SimaService(),
  );
  getIt.registerLazySingleton<LocalAuthentication>(
    () => LocalAuthentication(),
  );

  // Register BLoCs (depend on repositories and other services)
  getIt.registerFactory<AuthBloc>(
    () => AuthBloc(
      getIt<AuthRepository>(),
      getIt<RequestBuilder>(),
      getIt<FlutterSecureStorage>(),
    ),
  );
  getIt.registerFactory<HomeBloc>(
    () => HomeBloc(
      getIt<BankAccountsRepository>(),
      getIt<AuthRepository>(),
      getIt<RequestBuilder>(),
      getIt<FlutterSecureStorage>(),
      getIt<LocalAuthentication>(),
    ),
  );
}
