import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './hkj-template.reducer';

export const HkjTemplateDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const hkjTemplateEntity = useAppSelector(state => state.hkjTemplate.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="hkjTemplateDetailsHeading">
          <Translate contentKey="serverApp.hkjTemplate.detail.title">HkjTemplate</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{hkjTemplateEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="serverApp.hkjTemplate.name">Name</Translate>
            </span>
          </dt>
          <dd>{hkjTemplateEntity.name}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="serverApp.hkjTemplate.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{hkjTemplateEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="serverApp.hkjTemplate.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjTemplateEntity.createdDate ? (
              <TextFormat value={hkjTemplateEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="serverApp.hkjTemplate.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{hkjTemplateEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="serverApp.hkjTemplate.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjTemplateEntity.lastModifiedDate ? (
              <TextFormat value={hkjTemplateEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="serverApp.hkjTemplate.category">Category</Translate>
          </dt>
          <dd>{hkjTemplateEntity.category ? hkjTemplateEntity.category.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/hkj-template" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/hkj-template/${hkjTemplateEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HkjTemplateDetail;
