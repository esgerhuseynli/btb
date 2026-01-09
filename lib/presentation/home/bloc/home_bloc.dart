import 'package:flutter_bloc/flutter_bloc.dart';
import '../../core/bloc/base_bloc.dart';
import '../../../core/utils/request_builder.dart';
import '../../../data/repositories/bank_accounts_repository.dart';
import 'home_event.dart';
import 'home_state.dart';

class HomeBloc extends BaseBloc<HomeEvent, HomeState> {
  final BankAccountsRepository _bankAccountsRepository;
  final RequestBuilder _requestBuilder;

  HomeBloc(
    this._bankAccountsRepository,
    this._requestBuilder,
  ) : super(const HomeInitial()) {
    on<LoadBankCardsEvent>(_onLoadBankCards);
    on<LoadBankAccountsEvent>(_onLoadBankAccounts);
    on<RefreshHomeDataEvent>(_onRefreshHomeData);
  }

  Future<void> _onLoadBankCards(
    LoadBankCardsEvent event,
    Emitter<HomeState> emit,
  ) async {
    emit(const HomeLoading());
    try {
      // Use getCommonRequest() for authenticated requests (like Android Utils.getCommonRequest())
      // This sets SaltSignature to sessionKey and clears Username/PasswordHash
      final requestInfo = await _requestBuilder.getCommonRequest();

      final response = await _bankAccountsRepository.getBankCards(
        requestInfo: requestInfo,
      );

      if (response.responseInfo.isSuccess && response.bankCards != null) {
        final currentState = state;
        if (currentState is HomeLoaded) {
          emit(HomeLoaded(
            bankCards: response.bankCards!,
            bankAccounts: currentState.bankAccounts,
          ));
        } else {
          emit(HomeLoaded(
            bankCards: response.bankCards!,
            bankAccounts: const [],
          ));
        }
      } else {
        emit(HomeError(
          response.responseInfo.errorMessage ??
              response.responseInfo.responseMessage ??
              'Kartlar yüklənə bilmədi',
        ));
      }
    } catch (e) {
      emit(HomeError(e.toString()));
    }
  }

  Future<void> _onLoadBankAccounts(
    LoadBankAccountsEvent event,
    Emitter<HomeState> emit,
  ) async {
    final currentState = state;
    if (currentState is! HomeLoading) {
      emit(const HomeLoading());
    }

    try {
      // Use getCommonRequest() for authenticated requests (like Android Utils.getCommonRequest())
      // This sets SaltSignature to sessionKey and clears Username/PasswordHash
      final requestInfo = await _requestBuilder.getCommonRequest();

      final response = await _bankAccountsRepository.getBankAccounts(
        requestInfo: requestInfo,
      );

      if (response.responseInfo.isSuccess && response.bankAccounts != null) {
        final currentState = state;
        if (currentState is HomeLoaded) {
          emit(HomeLoaded(
            bankCards: currentState.bankCards,
            bankAccounts: response.bankAccounts!,
          ));
        } else {
          emit(HomeLoaded(
            bankCards: const [],
            bankAccounts: response.bankAccounts!,
          ));
        }
      } else {
        emit(HomeError(
          response.responseInfo.errorMessage ??
              response.responseInfo.responseMessage ??
              'Hesablar yüklənə bilmədi',
        ));
      }
    } catch (e) {
      emit(HomeError(e.toString()));
    }
  }

  Future<void> _onRefreshHomeData(
    RefreshHomeDataEvent event,
    Emitter<HomeState> emit,
  ) async {
    emit(const HomeLoading());
    try {
      // Use getCommonRequest() for authenticated requests (like Android Utils.getCommonRequest())
      // This sets SaltSignature to sessionKey and clears Username/PasswordHash
      final requestInfo = await _requestBuilder.getCommonRequest();

      final cardsResponse = await _bankAccountsRepository.getBankCards(
        requestInfo: requestInfo,
      );
      final accountsResponse = await _bankAccountsRepository.getBankAccounts(
        requestInfo: requestInfo,
      );

      if (cardsResponse.responseInfo.isSuccess &&
          accountsResponse.responseInfo.isSuccess) {
        emit(HomeLoaded(
          bankCards: cardsResponse.bankCards ?? const [],
          bankAccounts: accountsResponse.bankAccounts ?? const [],
        ));
      } else {
        emit(HomeError(
          cardsResponse.responseInfo.errorMessage ??
              accountsResponse.responseInfo.errorMessage ??
              'Məlumat yüklənə bilmədi',
        ));
      }
    } catch (e) {
      emit(HomeError(e.toString()));
    }
  }
}

